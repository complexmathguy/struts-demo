#!/bin/bash

cd /code/gitRoot/terraform

terraform version
terraform init --input=false 
terraform plan --input=false --out=terraform.plan -var host=https://xxx.xxx.xxx.xxx -var username= -var password= -var region=- -var project= 
terraform apply --input=false "terraform.plan"