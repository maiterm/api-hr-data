# api-hr-data

Rest API service in scala with playframework to comunicate with an Azure Data Base of HR.


## Installation

1. Clone the repository to your local machine.
2. Install sbt and java.
3. Run `sbt run` to start the server.
4. Use Postman to send requests to the API as are shown in the file conf/routes

## API Documentation

  

### POST  /api/jobs              
**Description**
Create multiple jobs in a batch. The request body should be a JSON array of objects, each representing a job.


**Parameters**
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





### POST  /api/departments   

**Description**
Create multiple departments in a batch. The request body should be a JSON array of objects, each representing a departments.


**Parameters**
- `id`: int (required)
- `department`: string (required)

#### Request Body
```json
[
  {
    "id": 1,
    "department": "Software"
  },
  {
    "id": 2,
    "department": "Data"
  } 
]
```
       
### POST  /api/employees/       

**Description**
Create multiple employees in a batch. The request body should be a JSON array of objects, each representing a employee.


**Parameters**
- `id`: int (required)
- `name`: string (required)
- `datetime`: datetime (required)
- `job_id`: int (required) - foreign key
- `department_id`: int (required) - foreign key

#### Request Body
```json
[
{
    "id": 1,
    "name": "Harold Vogt",
    "datetime": "2021-11-06T23:48:42-03:00",
    "departmentId": 2,
    "jobId": 96
},
{
    "id": 5,
    "name": "Gretna Lording",
    "datetime": "2021-10-10T19:22:17-03:00",
    "departmentId": 6,
    "jobId": 80
}
]
```


### Errors

The API may return the following errors in addition to the ones listed above:

- `400 BadRequest`: when the body is not the required or invalid.
- `409 Conflict`: when the API reconized in the request at least one id that already exists in the data base, or a foreign key does not exist.



### GET     /api/metrics/hiredbyquarter  
Number of employees hired for each job and department in 2021 divided by quarter, ordered alphabetically by department and job. 

#### Response example
```json
{
    "HiredByQuarter": [
        {
            "department": "Accounting",
            "job": "Account Representative IV",
            "Q1": 1,
            "Q2": 0,
            "Q3": 0,
            "Q4": 0
        },
        {
            "department": "Accounting",
            "job": "Actuary",
            "Q1": 0,
            "Q2": 1,
            "Q3": 0,
            "Q4": 0
        }
  ]
}
```

### GET     /api/metrics/hiredbydepartment

List of inds, name and number of employees hired of each department that hired more employees than the mean of employees hired in 2021, order by the descending number of employees hired. 

#### Response example
```json
{
    "HiredByDepartment": [
        {
            "id": 8,
            "department": "Support",
            "hired": 248
        },
        {
            "id": 5,
            "department": "Engineering",
            "hired": 242
        },
        {
            "id": 6,
            "department": "Human Resources",
            "hired": 242
        }
    ]
}
```


### GET     /api/backup/:tableName

Backup for each table, with tableName = ["employees", "jobs", "departments"] 


### GET  /api/restore/:tableName              
Restore for each table, with tableName =  ["employees", "jobs", "departments"] 


### GET  /api/departments/:id 
### GET  /api/employees/:id  
### GET  /api/jobs/:id

## Contact

Full Name: [Maite Rodriguez Muñiz](mailto:rodriguezmmaite@gmail.com)

Project Link: [https://github.com/maiterm/api-hr-data](https://github.com/maiterm/api-hr-data)
