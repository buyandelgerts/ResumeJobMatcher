# Resume Job Matcher App
This app helps check if your resume fits a job. It uses free AI on your computer. No costs or internet needed after setup.

## What It Does

Put in your resume (PDF or text).
Add job details.
Get match score, missing skills, better words for resume, and short summary.

## Tools

Java and Spring Boot.
Ollama for AI (free models like Gemma).
PDFBox for PDFs.
HTMX and Tailwind for looks.

## Setup Steps

Get code: git clone this code

Install Ollama: Go to ollama.com, download, run ollama pull gemma3:1b

Build app: ./mvnw clean install

Run: ./mvnw spring-boot:run

Open browser: http://localhost:8080

## How to Use

Upload resume or paste text.
Paste job description.
Click button – wait a bit for AI.
See results!
