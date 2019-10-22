# Picturegram
A fake instagram with reviews feature added

### Theoretical Scenarios
* Reviews become more complex (explanation, tags, geolocation, etc) and we need to have internal and external reports on them (with different information on them), which a particular team will work on.  

    **Thoughts :** We can extract the existing Reviews to a separate Microservice, and add the explaination, tags, geolocation etc attributes to the existing Review Model.  The existing API endpoints should be enhanced to  handle the extra attributes. Also, we would be adding new API endpoints for Data Processing. Making this a Microservice would give us the liberty to add new supporting models for review depending on the type of reports to be extracted. So, if there is a change needed in the way of extracting reports or any other functionality in Reviews, then we would modify only Reviews Microservice.
    
* We add the same functionality but for videos

    **Thoughts :** The ImagePost model needs to be modfied to something more generic like PicturegramPost. This model would inherit all the attributes from the ImagePost model, and instead of storing the image in byte array we would have to leverage a cloud infrastructure for storing the images and videos (similar to Amazon S3 buckets) and save the location of the image in our database.
    
* After adding videos it's a boom, people love it and use it extensively
    
    **Thoughts :** We would need to consider separating the concerns and create microservices for Authentication(Register, Login), Feed(Image/video Feed with Pageable feature to reduce the load on database), User Actions(Upload, delete, modify images/videos) and Reviews. Each service would be containerized and desinged to be resilient, fault tolerant to give the best user experience. We would leverage cloud(K8's or AWS or PCF) to setup Gateway and the load balancers to distribute load among the containers.  
    The application should be made highly available by deploying to different zones and as our application deals with a lot of images and videos mechanism for caching the feed, session data etc would be helpful to serve the content faster.

### Resources

**Register/Sign-up User:** POST http://localhost:8080/picturegram/api/v1/register

```json
{
  "emailId": "kevin@gmail.com",
  "password": "kevinspassword"
}
```

**Login User:** POST http://localhost:8080/picturegram/api/v1/login

```json
{
  "emailId": "kevin@gmail.com",
  "password": "kevinspassword"
}
```

**Upload Image :** POST http://localhost:8080/picturegram/user/api/v1/uploadImageByUser
```
Header: 
key = Content-Type, value = application/x-www-form-urlencoded
key = Authorization , value = Bearer [JWT token from Login API response]
Body : key = imageFile, value = [Browse a picture]
```


**Review Image :** POST http://localhost:8080/picturegram/reviews/api/v1/reviewPost
```
Header: 
key = Authorization , value = Bearer [JWT token from Login API response]
Body : 
key = rating, value = number value for review rating (Between 1 - 5)
key = imageId, value = number value for image ID
```

**Delete Image :** POST http://localhost:8080/picturegram/user/api/v1/deleteUserImage
```
Header: 
key = Authorization , value = Bearer [JWT token from Login API response]
Body : 
key = image_id, value = number value for image ID to be deleted
```

**Average reviews :** GET http://localhost:8080/picturegram/reviews/api/v1/averageImageReviews
```
Header: 
key = Authorization , value = Bearer [JWT token from Login API response]
```

### Steps to pull and run the Docker Image

- docker login

- docker run -p 8080:8080 bharathbrahma7/picgram

- Access the h2 console at http://localhost:8080/picturegram/h2 and use following:    
    a. **JDBC URL** : jdbc:h2:mem:picturegram  
    b. **Username/Password** : sa/password  
    
- **SWAGGER API SPECS:** A basic overview of the catalog of services is will be found on : http://localhost:8080/picturegram/swagger-ui.html#/

- Download and import the Postman-json file from : https://github.com/BharathBrahma/Picgram-sample-requests.git 

- Steps to execute the scenarios :  
    - Execute the POST request REGISTER for registering a user  
    - Execute LOGIN for the same user (Use the same credentials used to register)  
    - Copy the jwt token in the response of the login request (It will be timed out after 10 mins, so we might have to refresh if it gives token expired message)  
    - Paste the token into the Authorization header (Bearer <<token>>)of the UPLOAD request, and upload an image
    - Paste the token into the Authorization header of the REVIEW request,  refer h2 console to get the imageId, then use the Review request to modify the imageID and the reviewRating field.  
    - Paste the token into the Authorization header of the DELETE request,  refer h2 console to get the imageId, then use the Delete request to modify the image_id value to delete the image.