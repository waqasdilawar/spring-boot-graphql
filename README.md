# GraphQL with Spring Boot

### Available Operations
Following operations are available:

* Query `{
  findAllApplications{
  id
  owner
  description
  }
  }`
* Mutation `mutation{
  newApplication(name: "test", owner: "waqas", description: "desc") {
  id
  name
  description
  }
  }`
* Subscription `subscription {
  getRandomValue(symbol: "GOOG")
  }`



