import { email, helpers, required } from "@vuelidate/validators";

export const createCommentRule = {
    name: {
      required: helpers.withMessage("This field is required.", required),
    },
    email: {
        required: helpers.withMessage("The email field is required.", required),
        email: helpers.withMessage("The email field invalid.", email),
      },
    content: {
      required: helpers.withMessage("This field is required.", required),
    },

  };