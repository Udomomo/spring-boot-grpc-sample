import com.google.protobuf.gradle.id

// version catalogにlibs.versions.grpcとlibs.versions.grpc.kotlinがあるので、前者の値を取得したいならasProvider()を挟む必要がある。
// https://discuss.gradle.org/t/how-to-overwrite-version-from-catalog/46631
val grpcVersion: String = libs.versions.grpc.asProvider().get()
val grpcKotlinVersion: String = libs.versions.grpc.kotlin.get()
val protobufVersion: String = libs.versions.protobuf.get()
val grpcSpringBootStarterVersion: String = libs.versions.grpc.spring.boot.starter.get()
val exposedVersion: String = libs.versions.exposed.spring.boot.starter.get()

plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.kotlin.plugin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.protobuf)
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
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${exposedVersion}")
	implementation("net.devh:grpc-server-spring-boot-starter:${grpcSpringBootStarterVersion}")
	implementation(libs.grpc.protobuf)
	// Kotlinのstub実装を生成するために必要。
	implementation(libs.grpc.kotlin.stub)
	// gRPCの通信制御に必要。
	implementation(libs.grpc.netty.shaded)
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
