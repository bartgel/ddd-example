# Simple HTTP Server Framework

## A Practical Domain-Driven Design Demonstration

This project showcases a modern, reflection-free Java application framework built with Domain-Driven Design (DDD) principles. It demonstrates how to create a Spring Boot-like development experience without runtime reflection or external framework dependencies.

## 🚀 Key Features

- **Zero Reflection Runtime**: Pure compile-time code generation for optimal performance    
- **DDD Architecture**: Clean separation of concerns through well-defined bounded contexts    
- **Modular Design**: Independent domains for templating, web serving, JSON processing, and dependency injection    
- **Compile-Time Optimization**: All code generation happens during compilation, eliminating runtime overhead    
- **Modernization Blueprint**: Practical example for legacy modernization and greenfield projects
    

## 🏗️ Architecture Overview

The framework is organized into core domains:

- **Templating**: Compile-time Java class generation    
- **WebServing**: HTTP request handling and routing logic    
- **Json**: Efficient serialization/deserialization utilities    
- **Injector**: Dependency injection framework    

Each domain operates as an independent bounded context with clearly defined interfaces and contracts.

## 📚 Purpose & Vision

This project serves as both a functional framework and an educational resource demonstrating:

- How to implement DDD in modern Java applications    
- Practical approaches to framework design without external dependencies    
- Strategies for achieving high-quality, maintainable codebases    
- Real-world examples for articles and technical documentation    

The codebase will evolve but will maintain its focus on providing clear, practical insights into building well-architected software systems.

## 📄 License

This project is licensed under the **Mozilla Public License 2.0 (MPL-2.0)**.

### What this means:

- **You retain freedom**: Can use, modify, and distribute the code    
- **Changes must be published**: Modifications to licensed files must be shared back    
- **Project stewardship**: The original author maintains control over the project direction    
- **Commercial friendly**: Can be used in proprietary projects, with MPL-2.0 terms applying only to modified files    

For complete license details, see the [LICENSE](https://license/) file or visit [Mozilla's MPL 2.0 page](https://www.mozilla.org/en-US/MPL/2.0/).

## 🛠️ Getting Started

Building the code:

```bash
git clone git@github.com:bartgel/ddd-example.git
cd ddd-example
mvn clean install
```

Run the example webservice:

```bash
cd server-core
mvn exec:java -Dexec.mainClass=com.bart.example.SimpleHttp
```

Running eventcatalog

```bash
cd ddd-doc
npm run dev
```

## 🤝 Contributing

While this is primarily a demonstration project, thoughtful discussions and suggestions are welcome. Please note that all contributions will be subject to the MPL-2.0 license terms.

## 📖 Documentation

Explore the domain structure and architecture through our [EventCatalog](https://your-docs-link-here/) for detailed insights into the application's design and flow.

---

_This project demonstrates how strategic domain-driven design can lead to successful modernization efforts and high-quality software architecture._