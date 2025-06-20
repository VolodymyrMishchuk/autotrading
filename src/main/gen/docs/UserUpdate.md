

# UserUpdate


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**firstName** | **String** |  |  [optional] |
|**lastName** | **String** |  |  [optional] |
|**email** | **String** |  |  [optional] |
|**phoneNumber** | **String** |  |  [optional] |
|**birthDate** | **LocalDate** |  |  [optional] |
|**role** | [**RoleEnum**](#RoleEnum) |  |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) |  |  [optional] |
|**password** | **String** |  |  [optional] |



## Enum: RoleEnum

| Name | Value |
|---- | -----|
| CUSTOMER | &quot;ROLE_CUSTOMER&quot; |
| ADMIN | &quot;ROLE_ADMIN&quot; |
| SUPER_ADMIN | &quot;ROLE_SUPER-ADMIN&quot; |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;ACTIVE&quot; |
| DEACTIVATED | &quot;DEACTIVATED&quot; |



