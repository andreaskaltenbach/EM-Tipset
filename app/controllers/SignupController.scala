package controllers

import play.mvc.Controller

import models.User
import play.api.data.Forms._
import play.api.data.Form
import views.html
import play.api.mvc.Action
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api._
import play.api.mvc
import play.api.mvc._
import play.api.mvc.Results._


/**
 * @author andreas.kaltenbach
 * @since 2012-02-20
 */
object SignupController extends Controller {

 val signupForm:Form[User] = Form (
   // Define a mapping that will handle User values
   mapping(
     "name" -> text(minLength = 4),
     "email" -> email,

     // Create a tuple mapping for the password/confirm
     "password" -> tuple(
       "main" -> text(minLength = 6),
       "confirm" -> text
     ).verifying(
       // Add an additional constraint: both passwords must match
       "Passwords don't match", pw => pw._1 == pw._2
     )
   )
     // The mapping signature doesn't match the User case class signature,
     // so we have to define custom binding/unbinding functions
   {
     // Binding: Create a User from the mapping result (ignore the second password and the accept field)
     (name, email, passwords) => User(name, passwords._1, email)
   }
   {
     // Unbinding: Create the mapping values from an existing User value
     user => Some(user.name, user.email, (user.password, ""))
   }.verifying(
     // Add an additional constraint: The username must not be taken (you could do an SQL request here)
     "This username is not available",
     user => !Seq("admin", "guest").contains(user.name)
   )
 )

  /**
   * Display an empty form.
   */
  def form = Action {
    Ok(html.signup.form(signupForm));
  }

  def summary = Action {
    println("Summary!!!")
    Ok(html.signup.form(signupForm));
  }

  /**
   * Handle form submission.
   */
  def login = Action { implicit request =>
    signupForm.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => BadRequest(html.signup.form(errors)),

      // We got a valid User value, display the summary
      user => Ok(html.signup.summary(user))
    )
  }
}
