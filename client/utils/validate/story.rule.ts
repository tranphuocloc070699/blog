import { required, email, minLength, helpers } from "@vuelidate/validators";
export const createStoryRule = {
  title: {
    required: helpers.withMessage("This field is required.", required),
  },
  content: {
    required: helpers.withMessage("This field is required.", required),
  },
  pre_content:{
    required: helpers.withMessage("This field is required.", required),
  },
  thumbnail: {
    required: helpers.withMessage("This field is required.", required),
  },
};


export const updateStoryRule = {
  title: {
    required: helpers.withMessage("This field is required.", required),
  },
  content: {
    required: helpers.withMessage("This field is required.", required),
  },
  preContent:{
    required: helpers.withMessage("This field is required.", required),
  },
  thumbnail: {
    required: helpers.withMessage("This field is required.", required),
  },
};