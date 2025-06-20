

# Account


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** |  |  [optional] |
|**balance** | **Double** |  |  [optional] |
|**currency** | **String** |  |  [optional] |
|**number** | **Integer** | Unique account number (e.g. MetaTrader ID) |  [optional] |
|**createdAt** | **OffsetDateTime** |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  [optional] |
|**personId** | **UUID** |  |  [optional] |
|**sourceId** | **UUID** |  |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) |  |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;ACTIVE&quot; |
| DEACTIVATED | &quot;DEACTIVATED&quot; |



