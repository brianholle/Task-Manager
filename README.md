# Simple Task Manager

This is a simple task management application that can be set up and used by one user to keep track of their tasks.

![Task Manager Preview](https://github.com/brianholle/Task-Manager/blob/master/resources/Preview.PNG)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Here is a list of things you will need to get the task manager up and running:

[Maven](https://maven.apache.org/download.cgi)

[Docker](https://www.docker.com/)


### Installing

A step by step series of examples that tell you how to get a development env running

1. Clone this repository

```
git clone https://github.com/brianholle/Task-Manager.git
```

Enter the downloaded folder

```
cd Task-Manager
```

Run the appropriate set up script for Windows/Linux systems.
```
sh taskManagerDockerRun.sh
```

If you have any issues with the install script, review the output, remove any images
the process has added, and re-attempt.

At this point, if everything has ran successfully, you should be able to access the task manager
at http://localhost:8090/

## Further Research

If you are interested in running this project locally either for reasearch or contribution purposes, here are a few more tips
to get you off of the ground. 

If you only wish to run/edit the front-end outside of the docker environment, you will need to make the following changes.
Firstly, modify the [Constants.java](https://github.com/brianholle/Task-Manager/blob/master/taskManager/src/main/java/com/taskManager/demo/Constants.java) file found in the 'taskManager' project. You will need to change the `API_BASE_URL` value to point to your localhost. 
  Ex: `http://localhost:8080/taskManager`
  
If you wish to run/edit the back-end of this project outside of docker, you will need to modify the [application.properties](https://github.com/brianholle/Task-Manager/blob/master/taskManagerRestService/src/main/resources/application.properties) file to point to your local database.
  Ex. `jdbc:postgresql://localhost:5432/task_manager`


## Authors

* **Brian Holle**
