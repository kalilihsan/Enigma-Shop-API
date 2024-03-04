### Profile Info

- Request

```json
{
  "firstName": "string optional",
  "lastName": "string optional",
  "domicile": "string optional",
  "position": "string optional",
  "bio": "string optional",
  "mobilePhoneNo": "string optional",
  "email": "string required",
  "address": "string optional"
}
```

- Response

```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string",
  "domicile": "string",
  "position": "string",
  "bio": "string",
  "mobilePhoneNo": "string",
  "email": "string"
}
```

### Ubah Password

- Request

```json
{
  "password": "string",
  "newPassword": "string",
  "confirmPassword": "string"
}
```

- Response Bebas

### Peserta

- Request

```json
{
  "name": "string required",
  "locationTest": "string required",
  "email": "string required",
  "birthDate": "string required"
}
```

- Response

```json
{
  "id": "string",
  "name": "string",
  "locationTest": "string",
  "email": "string",
  "birthDate": "string",
  "age": 0
}
```

### Admin

- Request

```json
{
  "nip": "string required",
  "name": "string required",
  "email": "string required",
  "position": "string required"
}
```

### Lokasi Tes

- Request

```json
{
  "outletCode": "string required",
  "outletName": "string required",
  "city": "string required",
  "province": "string required"
}
```

- Response

```json
{
  "id": "string",
  "outletCode": "string required",
  "outletName": "string required",
  "city": "string required",
  "province": "string required"
}
```