

# User


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** |  |  [optional] |
|**firstName** | **String** |  |  [optional] |
|**lastName** | **String** |  |  [optional] |
|**email** | **String** |  |  [optional] |
|**phoneNumber** | **String** |  |  [optional] |
|**birthDate** | **LocalDate** |  |  [optional] |
|**role** | [**RoleEnum**](#RoleEnum) |  |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) |  |  [optional] |



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
| PENDING_CONFIRMATION | &quot;PENDING_CONFIRMATION&quot; |



