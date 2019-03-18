# SEDEApi

AWS lambda to query SEDE (Stack Exchange Data Explorer), library can also be used to directly access SEDE (get query result both as cvs and/or object)

The lambda gets from header an JWT api tooken containg login credentials, logs in to SE and then execute the query returning result as json.

##Usage
 
To be able to access the lamba an autentication token and address to lambda needs to be request from SOBotics. 
 
 The request is executed as a GET request to
 
    https://path.to.lambda/[site]/[idQuery]?queryParams
    
with JWT api token in header name 'bearer' also ContentType='application/json' should be set. The result is return as a json array where every record as as attribute column name and as value the value of record.

Example:

   https://data.stackexchange.com/stackoverflow/query/7521/how-unsung-am-i with UserId:5292302 would be
   
Request:
   
   https://path.to.lambda/stackoverflow/7521?UserId=5292302 
   
Result:

{
    "result": [
        {
            "Accepted Answers": "334",
            "Scored Answers": "272",
            "Unscored Answers": "62",
            "Percentage Unscored": "18.5"
        }
    ]
}
  
