import com.google.protobuf.gradle.id

val grpcVersion: String by extra("1.70.0")
val grpcKotlinVersion: String by extra("1.4.0")
// protocはv4.26で破壊的変更が入り、grpc-javaがまだそれに対応していないので、v3.25を使用する。
// https://github.com/grpc/grpc-java/issues/10976
val protobufVersion: String by extra("3.25.5")

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.protobuf") version "0.9.5"
	id("idea")
}

group = "com.udomomo"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.h2database:h2:2.2.224")
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.60.0")
	implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
	implementation("io.grpc:grpc-protobuf:${grpcVersion}")
	// Kotlinのstub実装を生成するために必要。
	implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
	// gRPCの通信制御に必要。
	implementation("io.grpc:grpc-netty-shaded:${grpcVersion}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// protoファイルをprojectの外に置いているので、pathを指定して認識させる。
sourceSets {
	main {
		proto {
			srcDir("${rootDir}/proto")
		}
	}
}

protobuf {
	// protobufスキーマのコンパイラのartifactを指定する。
	protoc {
		artifact = "com.google.protobuf:protoc:${protobufVersion}"
	}
	// stubの生成に必要なpluginを指定する。
	plugins {
		// KotlinのstubはJavaコードにも依存するため、こちらも指定する必要がある。
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
		}
		id("grpckt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
		}
	}
	// generateProtoTasksで、上のpluginを利用する。
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc")
				id("grpckt")
			}
		}
	}
}

// 生成されたコードをintellij上で認識させる。
idea {
	module {
		listOf("java", "grpc", "grpckt").forEach { dir ->
			sourceSets.getByName("main").java { srcDir("$buildDir/generated/source/proto/main/$dir") }
		}
	}
}


tasks.withType<Test> {
	useJUnitPlatform()
}
