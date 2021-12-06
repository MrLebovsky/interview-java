package uk.ac.wlv.smells;

@Transactional
public class GradeService {
    private final LecturerRepository lecturerRepository;

    public void printAverageGrades() {
		int[] grades1 = getModuleGrades("4CS001");
		int sum4CS001 = 0;
		for(Integer i = 0; i < grades1.length; i++) {
			sum4CS001 += grades1[i];
		}
		int averageGrade4CS001 = sum4CS001 / grades1.length;
		
		int[] grades2 = getModuleGrades("6CS002");
		int sum6CS002 = 0;
		for(Integer i = 0; i < grades2.length; i++) {
			sum6CS002 += grades2[i];
		}
		int averageGrade6CS002 = sum6CS002 / grades2.length;
		
		int[] grades3 = getModuleGrades("5CS003");
		int sum5CS003 = 0;
		for(Integer i = 0; i < grades3.length; i++) {
			sum5CS003 += grades3[i];
		}
		int averageGrade5CS003 = sum5CS003 / grades3.length;
		
		System.out.println("4CS001 Average: " + averageGrade4CS001);
		System.out.println("6CS002 Average: " + averageGrade6CS002);
		System.out.println("5CS003 Average: " + averageGrade5CS003);
	}

    public GradeClassification calculateGradeClassification(int studentNo) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		String SQL = "SELECT * FROM grades where studentNo =" + studentNo;
		String url = "jdbc:mysql://localhost:3306/grades";
		try(Connection connection = DriverManager.getConnection(url, "root", "password")) {
			try(Statement stmt = connection.createStatement()) {
				try (ResultSet rs = stmt.executeQuery(SQL)) {
					List<Integer> gradesList = new ArrayList<Integer>();
					while (rs.next()) {
						gradesList.add(rs.getInt("grade_percentage"));
					}
					int sum = 0;
					for(int grade : gradesList) {
						sum += grade;
					}
					int average = sum / gradesList.size();
					if(average >= 70) {
						return GradeClassification.FIRST_CLASS_HONOURS;
					} else if (average >= 60) {
						return GradeClassification.UPPER_SECOND_CLASS_HONOURS;
					} else if (average >= 50) {
						return GradeClassification.LOWER_SECOND_CLASS_HONOURS;
					} else {
						return GradeClassification.THIRD_CLASS_HONOURS;
					}
				}
			}
		} catch (SQLException e) {
			return null;
		}
	}

    public Lecturer findLecturer(int lecturerNo) {
        return lecturerRepository.findAll()
                    .stream()
                    .filter(lecturer -> lecturer.getNo() == lecturerNo)
                    .findFirst().orElse(null);
    }
	
}
