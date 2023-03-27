# api-hr-data

Rest API service in scala with playframework to comunicate with a data base of HR.


## Installation

1. Clone the repository to your local machine.
2. Install sbt and java.
3. Run `sbt run` to start the server.
4. Use Postman to send requests to the API as are shown in the file conf/routes

## API Documentation

  

### POST  /api/jobs              
**Descripción**
Create multiple jobs in a batch. The request body should be a JSON array of objects, each representing a job.


**Parámetros**
- `id`: int (required)
- `job`: string (required)


#### Request Body
```json
[
  {
    "id": 1,
    "job": "Software Engineer"
  },
  {
    "id": 2,
    "job": "Data Analyst"
  } 
]
```

#### Errors

The API may return the following errors in addition to the ones listed above:

- `400 BadRequest`: when the body is not the required.
- `409 Conflict`: when the API reconized in the request at least one id that already exists in the data base.



### POST  /api/departments   
**Parámetros**
- `id`: int (required)
- `department`: string (required)


       
### POST  /api/employees/         
**Parámetros**
- `id`: int (required)
- `name`: string (required)
- `datetime`: datetime (required)
- `job_id`: int (required)
- `department_id`: int (required)

### GET  /api/departments/:id 
### GET  /api/employees/:id  
### GET  /api/jobs/:id

## Contact

Full Name: [Maite Rodriguez Muñiz](mailto:rodriguezmmaite@gmail.com)

Project Link: [https://github.com/maiterm/api-hr-data](https://github.com/maiterm/api-hr-data)
