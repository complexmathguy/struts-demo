resource "kubernetes_replication_controller" "app-master" {
  metadata {
    name = "app-master"
  }

  spec {
    replicas = 1

    selector = {
      app  = "strutsdemo"
    }
    template {        
    template {
      container {
        image = "mysql"
        name  = "mysql"

        port {
          container_port = 3606
        }

        resources {
          requests {
            cpu    = "100m"
            memory = "100Mi"
          }
        }
      }

      container {
        image = "theharbormaster/struts-demo:latest"
        name  = "app-container"
        
        env {
          name  = "DATABASE_USERNAME"
          value = "root"                 
        }

        env {
          name  = "DATABASE_PASSWORD"
          value = ""                 
        }

        env {
          name  = "DATABASE_DIALECT"
          value = "org.hibernate.dialect.MySQL8Dialect"                 
        }

        env {
          name  = "DATABASE_DRIVER"
          value = "com.mysql.cj.jdbc.Driver"                 
        }

        env {
          name  = "DATABASE_URL"
          value = jdbc:mysql://${kubernetes_service.app-master.load_balancer_ingress.0.ip}:3306/strutsdemo?createDatabaseIfNotExist=true                 
        }
        
        port {
          container_port = 8080
        }

        port {
          container_port = 3606
        }

        resources {
          requests {
            cpu    = "100m"
            memory = "100Mi"
          }
        }
      }
    }
    }
  }
}