docker pull postgres
cd taskManager
mvn package
docker build . -t task-manager-frontend
cd ..
cd taskManagerRestService
mvn package
docker build . -t task-manager-backend
cd ..
docker run --name task-postgres -e POSTGRES_DB=task_manager -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=password -d postgres
docker run -p 8080:8080 --name task-manager-backend --link task-postgres:postgres -d task-manager-backend
docker run -p 8090:8090 --name task-manager-frontend --link task-manager-backend:task-manager-backend -d task-manager-frontend