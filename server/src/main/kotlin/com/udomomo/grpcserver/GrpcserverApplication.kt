package com.udomomo.grpcserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcserverApplication

fun main(args: Array<String>) {
	runApplication<GrpcserverApplication>(*args)
}
