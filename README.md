# Secure File System Web API

### To get the project up and running you will need:

1) PostgreSQL RDMS installed on your local machine.
2) Previously created database scheme with a name `sec_file_system_db`, if you don't want to change basic project configuration.
3) Project can work in two profiles:

   - `dev` — working with the project code in a development mode
   - `test` — for running integration tests using H2 database
   - To switch between profiles, specify an active profile in [application.yml](src/main/resources/application.yml) by declaring one of these two options

## Web API overview
<hr/>

## User API
<hr/>

GET — `/api/user` **_Get a list of users_**
> **_Response Body_**

```
[
   {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "johndoe@gmail.com",
      "phoneNumber": "123-456-7890",
      "age": 30
   },
                  .......
]
```
> **_Response Status_**

<p style="color:green;">Success 200 OK</p>
<p style="color:red;">Failure 400 BAD REQUEST</p>
<hr/>

POST — `/api/user` **_Create a user with unique credentials_**
> **_Request Body_**

```
{
    "firstName": "Kayle",
    "lastName": "Storm",
    "email": "kayle.storm@gmail.com",
    "phoneNumber": "124-567-7865",
    "age": 20
}
```
> **_Response Body_**

```
{
    "id": 16,
    "firstName": "Kayle",
    "lastName": "Storm",
    "email": "kayle.storm@gmail.com",
    "phoneNumber": "124-567-7865",
    "age": 20
}
```
> **_Response Status_**

<p style="color:green;">Success 201 CREATED</p>
<p style="color:red;">Failure 400 BAD REQUEST</p>

*Failure response body example*

```
{
    "firstName": "must not be blank",
    "http_message": "400 BAD_REQUEST",
    "status": "400"
}
```
<hr/>

PUT — `/api/user/{id}` **_Update a user by id_**
> **_Path Variable_** id - `16`

> **_Request Body_**

```
{
    "firstName": "Kayle",
    "lastName": "Smith",
    "email": "kayle.storm@gmail.com",
    "phoneNumber": "124-567-7865",
    "age": 30
}
```
> **_Response Body_**

```
{
    "id": 16,
    "firstName": "Kayle",
    "lastName": "Smith",
    "email": "kayle.storm@gmail.com",
    "phoneNumber": "124-567-7865",
    "age": 30
}
```
> **_Response Status_**

<p style="color:green;">Success 201 CREATED</p>
<p style="color:red;">Failure 400 BAD REQUEST</p>

*Failure response body example*

```
{
    "phoneNumber": "must not be blank",
    "http_message": "400 BAD_REQUEST",
    "status": "400"
}
```
<hr/>

DELETE — `/api/user/{id}` **_Delete a user by id_**
> **_Path Variable_** id - `16`

> **_Response Status_**
<p style="color:green;">Success 200 OK</p>
<hr/>

## File API
<hr/>