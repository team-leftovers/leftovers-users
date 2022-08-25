Users Microservice
===
This microservice will contain the required endpoints and database connections to create accounts of varying types, such as Customers, Web Admins, Restaurant Admins, and Drivers.
Currently, the Microservice only contains endpoints for creating, querying, and deleting customer accounts, which results in the following endpoints:

## `/customer`
### GET:
- Returns a JSON list of customers (defined at the bottom of this document)
- If no customers have been registered, the endpoint will return code 204 (No Content), on browsers this will result in the current page not being changed, this is intended behaviour

### POST:
- Creates a new customer using a JSON object (defined at the bottom of this document)
- On successful creation of the customer, code 201 will be returned alongside a JSON object representing the customer (defined at the bottom of this document) and the `Location` header will contain a url to an endpoint that's created. (e.g. `/customer/20` for a customer that's created with id 20)
- On duplicate email, returns code 409
- On invalid JSON, returns code 400

## `/customer/email/{email}`
### GET:
- Searches for and returns a customer based on a passed in {email}.
- Returns code 200 and the JSON representation of the customer (defined at the bottom of this document) on success
- Returns code 404 on failure to find the customer
- Returns code 400 when passing an invalid email or no email

## `/customer/{customerId}`
### GET:
- Ditto for the /customer/email/{email} endpoint but instead searching based on a customer id.

### DELETE:
- Deletes the customer account identified by the passed in ID, returning code 200 on success
- Returns code 404 on failure to find the customer
- Returns code 400 when passing an invalid id or no id

### PUT:
- Updates a customer account identified by the passed in ID using a JSON body (defined at the bottom of this document), returning code 200 and the updated customer on success
- Returns code 404 on failure to find the customer
- Returns code 400 when passing an invalid id or invalid JSON body.

## `/login`
### POST:
- Takes an email and password and creates a JWT Token for use in authorizing with the other endpoints

## `/logout`
### GET:
- Invalidates the JWT Token for the active user, effectively 'logging out'.

Customer JSON:
```json
{
	"id": int,
	"firstName": string,
	"lastName": string,
	"email": string,
	"phoneNo": string, // No special formating (e.g. instead of (123) 456-7890 it's 1234567890)
	"hashedPassword": string, // To be removed
	"type": enum["R", "D", "C", "S"], // R: Restaurant Admin, D: Driver, C: Customer, S: Site Admin
	"address": {
		"id": int,
		"latitude": real,
		"longitude": real,
		"zipCode": int, // Soon to be changed to string
		"country": "US", // This never changes
		"streetAddress": string,
		"houseNumber": string?, // Nullable
		"unitNumber": string? // Nullable
	}
}
```

Customer Creation JSON:
```json
{
	"firstName": string,
	"lastName": string,
	"email": string,
	"phoneNo": string,
	"password": string,
	"addressLine": string,
	"houseNumber": string?, // Nullable
	"unitNumber": string?, // Nullable
	"city": string,
	"state": string,
	"zipcode": string
}
```

Customer Update JSON:
```json
{
	"firstName": string, // Nullable
	"lastName": string, // Nullable
	"email": string, // Nullable
	"phoneNo": string, // Nullable
	"password": string, // Nullable
	"addressLine": string, // Nullable
	"houseNumber": string?, // Nullable
	"unitNumber": string?, // Nullable
	"city": string, // Nullable
	"state": string, // Nullable
	"zipcode": string // Nullable
}
```

Error JSON:
```json
{
  "error": string,
  "status": int // HTTP Status Code
}
```

Error JSON (Passing in Invalid JSON Body):
```json
{
  "error": string,
  "message": {
    "<field>": string // What's wrong with the field,
    ...
  },
  "status": int // HTTP Status Code
}
```

Example of Invalid JSON response:
```json
{
  "error": "Invalid field(s) in request.",
  "message": {
    "zipcode": "Zipcode is mandatory, must not be null",
    "phoneNo": "Phone number must be between 10 and 15 numbers"
  },
  "status": 400
}
```