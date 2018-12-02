#!/bin/bash
#Get Postgres Image
docker pull postgres

#Build Frontend Image
docker build taskManager/ -t task-manager-frontend

#Build Backend Image
docker build taskManagerRestService/ -t task-manager-backend

#Run Database Container
docker run --name task-postgres -e POSTGRES_DB=task_manager -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=password -d postgres
#Run REST API and DB
docker run -p 8080:8080 --name task-manager-backend --link task-postgres:postgres -d task-manager-backend
#Run Task Manager Frontend
docker run -p 8090:8090 --name task-manager-frontend --link task-manager-backend:task-manager-backend -d task-manager-frontend