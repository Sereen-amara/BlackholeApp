# BlackholeApp

## Project Overview
BlackholeApp is a Spring Boot application 


# Vulnerable Version 

This branch contains intentionally insecure code for Continous Assessment.

## Vulnerabilities include:
- SQL
- XSS
- Sensitive information


  ## How to Test:
- SQL Injection: Use `' OR 1=1; --` in search fields.
- XSS Enter `<script>alert('XSS')</script>` in input fields.
- Sensitive Data Exposure: Check file `/secrets.txt` through browser or through prject files.
