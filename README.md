To build and run the application, run the following PowerShell:

```
$env:CLIENT_SECRET="Application client secret"
$env:CLIENT_ID="Application client ID"
$env:TENANT_ID="Azure AD tenant ID"
$env:API_URL_ID="Application API URI"

.\mvnw spring-boot:run
```


Or the following Bash:

```
export CLIENT_SECRET="Application client secret"
export CLIENT_ID="Application client ID"
export TENANT_ID="Azure AD tenant ID"
export API_URL_ID="Application API URI"

./mvnw spring-boot:run 
```