package id.logicque.microservices

import id.logicque.microservices.repository.Repoimpl
import id.logicque.microservices.repository.Repository

class MicroService {
  val repository: Repository = Repoimpl()
}