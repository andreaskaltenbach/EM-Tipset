package controllers

import play.api.mvc._
import views.html

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.welcome())
  }
  
  def login = Action {
    println("login")
    Ok(views.html.welcome())
  }



}