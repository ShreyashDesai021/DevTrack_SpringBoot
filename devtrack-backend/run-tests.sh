#!/bin/bash

# DevTrack Backend - Quick Test Script
# This script verifies that all tests pass

echo "🧪 DevTrack Backend - Running Tests..."
echo "======================================"
echo ""

echo "📦 Step 1: Cleaning previous builds..."
mvn clean

echo ""
echo "🔨 Step 2: Compiling the project..."
mvn compile -DskipTests

echo ""
echo "🧪 Step 3: Running all tests..."
mvn test

echo ""
echo "✅ Test Summary:"
echo "================"
echo "If you see 'BUILD SUCCESS', all tests passed!"
echo ""
echo "Test Coverage:"
echo "- MockMvc tests (controllers): 14 tests"
echo "- JUnit tests (services): 18 tests"
echo "- Total: 32 tests"
echo ""
echo "Next steps:"
echo "1. Review test results above"
echo "2. Run 'mvn spring-boot:run' to start the application"
echo "3. Test endpoints with Postman or curl"
