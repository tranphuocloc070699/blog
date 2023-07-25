package com.loctran.server.util;

public  class LoggerColor {
    public enum Color {
        GREEN("\u001B[32m"),
        RED("\u001B[31m"),
        BLUE("\u001B[34m"),
        YELLOW("\u001B[33m"),
        RESET("\u001B[0m");
        
        private final String code;
        
        Color(String code) {
            this.code = code;
        }
        
        public String getCode() {
            return code;
        }
    }
    
    
    public static void println(Color color, String message) {
        System.out.println(color.getCode() + message + Color.RESET.getCode());
    }
   
}




