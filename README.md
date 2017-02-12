To run: `sbt run`
App will use in-memory DB (H2).

To test: `sbt test`

How to use:

To get all existing adverts - `GET http://localhost:9000/caradverts`
Response: `[$entity, $entity]`, where `entity` is described below.


To get one advert by ID - `GET http://localhost:9000/caradverts/$ID`
Response:
```
{
  "id":"$ID"
  "title":"$title",
  "fuel":"$fuel",
  "price":$price,
  "isNew":true|false,
  "mileage":$mileage(optional),
  "firstRegistration":$year(optional)
}
```
where `ID`- `String`,
`fuel` - one of [`gasoline`, `diesel`], (can be added as `GET http://localhost:9000/caradverts/fuel`)
`firstRegistration` - year in range 1901-current
`price` - `Int > 0`
`mileage` and `firstRegistration` - optional, if `isNew==false`


To create new advert - `POST http://localhost:9000/caradverts` with body:
```
{
  "title":"$title",
  "fuel":"$fuel",
  "price":$price,
  "isNew":true|false,
  "mileage":$mileage(optional),
  "firstRegistration":$year(optional)
}
```
response is the same.


To update advert - `PUT http://localhost:9000/caradverts/$ID`
with same body format as for create and same response.


To delete advert by ID - `DELETE http://localhost:9000/caradverts/$ID`
