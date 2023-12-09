package se.kth.iv1351.sem4.model;


// REmove if unnecessary
public class StudentsRentingDTO {
    private int studentID;
    private int rentalID;
   
   
    public StudentsRentingDTO(int studentID, int rentalID) {
        this.studentID = studentID;
        this.rentalID = rentalID;
    }


    public int getStudentID() {
        return studentID;
    }


    public int getRentalID() {
        return rentalID;
    }



    
}
