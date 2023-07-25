import { required, email, minLength, helpers } from "@vuelidate/validators";
export const signInRule = {
  email: {
    required: helpers.withMessage("The email field is required.", required),
    email: helpers.withMessage("The email field invalid.", email),
  },
  password: {
    required: helpers.withMessage("The password field is required.", required),
    minLength: helpers.withMessage(
      "The password field less than 3 characters",
      minLength(3)
    ),
  },
};

export const registerRule = {
  name:{
    required:helpers.withMessage("The username field is required.", required),
    minLength: helpers.withMessage(
      "The username field less than 2 characters",
      minLength(2)
    ),
  },
  email: {
    required: helpers.withMessage("The email field is required.", required),
    email: helpers.withMessage("The email field invalid.", email),
  },
  password: {
    required: helpers.withMessage(
      "The password field is required.",
      required
    ),
    minLength: helpers.withMessage(
      "The password field less than 3 characters",
      minLength(3)
    ),
  },
}
