# POC - Convert Excel or CSV files to POJO.

## Goal:

Provide an endpoin to process different files to POJO, the files could be in different formats(excel or csv), also they may have different column headers or maybe we need to do extra mapping or set some constants.

## Solution:

Use [Apache Poi](https://poi.apache.org/) and [Poiji](https://github.com/ozlerhakan/poiji?tab=readme-ov-file) libraries to create different schemas and mapping the files to POJO list.

Use [Jakarta Validation](https://beanvalidation.org/) and instanciate a Validator to validate the quality of the data and provide a meaningful error list to the stakeholders.

Develop a new Structure Selector class to evaluate which column structure was closer to the file provided. Other alternative studied but not in the main branch are, select mapper by fileName or by Query Param.

## Results:

### Happy path:
#### Request:

file in .xlsx with column structure '[name,language,email]'
```curl
curl --location 'localhost:8080/leads/file' \
--form 'file=@"/Users/gerard.puig/Desktop/lead_list.xlsx"'
````
file in .csv same structure
```curl
curl --location 'localhost:8080/leads/file' \
--form 'file=@"/Users/gerard.puig/Desktop/lead_list.csv"'
````
file in .xlsx and with structure '[Vorname,Sprache,E-Mail]'
```curl
curl --location 'localhost:8080/leads/file' \
--form 'file=@"/Users/gerard.puig/Desktop/lead_list_format2.xlsx"'
```

#### Response 
`200`:

```json
[
    {
        "name": "lead1",
        "language": "de_CH",
        "email": "lead1@test.com"
    },
    {
        "name": "lead2",
        "language": "it_CH",
        "email": "lead2@test.com"
    }
]
```


### Lead data not acomplish fiele validation rules:

#### Request:

Leads without madatory fields or email in wrong format:
```curl
curl --location 'localhost:8080/leads/file' \
--form 'file=@"/Users/gerard.puig/Desktop/leads_with_errors.xlsx"
````

#### Response:
`417`:

```json
{
    "title": "Errors in leads data.",
    "detail": "The leads data in the file has errors, please fix them and retry.",
    "status": 417,
    "invalidParams": [
        {
            "name": "Error for lead in row: 4",
            "reason": "'email' 'must be a well-formed email address'"
        },
        {
            "name": "Error for lead in row: 5",
            "reason": "'name' 'size must be between 0 and 6'"
        },
        {
            "name": "Error for lead in row: 6",
            "reason": "'name' 'must not be blank'"
        }
    ]
}
```

### Wrong header structure:

#### Request:
File with not supported header structure
```curl
curl --location 'localhost:8080/leads/file' \
--form 'file=@"/Users/gerard.puig/Desktop/lead_list_wrong_title.xlsx"'
````

##### Response 
`417`:

```json
{
    "title": "Missing expected columns.",
    "detail": "The following columns were not found [language, email, name]",
    "status": 417,
    "invalidParams": []
}
```

### File too large:

#### Request:
File with more thant 2mb (it can be configured in the application.yml)
```curl
curl --location 'localhost:8080/leads/file' \
--form 'file=@"/Users/gerard.puig/Desktop/lead_list_big_file.xlsx"'
````
##### Response:
`417`:

```json
{
    "type": "about:blank",
    "title": "Payload Too Large",
    "status": 413,
    "detail": "Maximum upload size exceeded",
    "instance": "/leads/file"
}
```


```
