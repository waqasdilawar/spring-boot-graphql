# GraphQL with Spring Boot - [Demo URL](https://still-shore-79504.herokuapp.com/graphiql)

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



