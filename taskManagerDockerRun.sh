#!/bin/bash
#Get Postgres Image
docker pull postgres

#Build Frontend Image
cd Task_Manager_Frontend
mvn package
docker build . -t task-manager-frontend
cd ..

#Build Backend Image
cd Task_Manager_Backend
mvn package
docker build . -t task-manager-backend
cd ..

#Run Database Container
docker run --name task-postgres -e POSTGRES_DB=task_manager -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=password -d postgres
#Run REST API and DB
docker run -p 8080:8080 --name task-manager-backend --link task-postgres:postgres -d task-manager-backend
#Run Task Manager Frontend
docker run -p 8090:8090 --name task-manager-frontend --link task-manager-backend:task-manager-backend -d task-manager-frontend