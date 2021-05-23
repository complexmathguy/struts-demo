resource "kubernetes_service" "app-master" {
  metadata {
    name = "app-master"
  }

  spec {
    selector = {
      app  = "struts-demo"
    }
    port {
      name        = "app-port"
      port        = 8080
      target_port = 8080
    }

    port {
      name        = "mysql-port"
      port        = 3306
      target_port = 3306
    }

    type = "LoadBalancer"
  }
  
}
