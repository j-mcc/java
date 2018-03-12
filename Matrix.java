
public class Matrix {
	
	private Double[][] matrix;
	
	public Double[][] asArray(){
		return matrix;
	}
	
	public Matrix(int height, int width, double cellValue){
		matrix = new Double[height][width];
		for(int r = 0; r < height; r++){
			for(int c = 0; c < width; c++){
				matrix[r][c] = cellValue;
			}
		}
	}
	
	public Matrix(int height, int width){
		matrix = new Double[height][width];
	}
	
	public Matrix(double[][] array){
		if(array != null){
			matrix = new Double[array.length][array[0].length];
			for(int r = 0; r < matrix.length; r++){
				for(int c = 0; c < matrix[r].length; c++){
					matrix[r][c] = array[r][c];
				}
			}
		}
	}
	
	public static String toString(Matrix matrix){
		if(matrix == null) return "";
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < getHeight(matrix); i++){
			for(int j = 0; j < getWidth(matrix); j++){
				stringBuilder.append(String.format("%6.6f\t", getCell(matrix, i, j)));
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
	
	public static Integer getHeight(Matrix matrix){
		if(matrix != null) return matrix.matrix.length;
		return null;
	}
	
	public static Integer getWidth(Matrix matrix){
		if(matrix != null) return matrix.matrix[0].length;
		return null;
	}
	
	public static Double getCell(Matrix matrix, int row, int col){
		if(matrix != null) return matrix.matrix[row][col];
		return null;
	}
	
	public static void setCell(Matrix matrix, int row, int col, double value){
		if(matrix != null) matrix.matrix[row][col] = value;
	}
	
	public static Matrix getCofactorMatrix(Matrix matrix){
		if(matrix == null) return null;
		if(getHeight(matrix) != getWidth(matrix)) return null;
		
		Matrix comatrix = new Matrix(getHeight(matrix), getWidth(matrix), 0);
		int sign = 1;
		for(int row = 0; row < getHeight(matrix); row++){
			for(int col = 0; col < getWidth(matrix); col++){
				Matrix sub = getSubMatrix(matrix, row, col);
				sign = (int) Math.pow(-1., row+1 + col+1);
			    setCell(comatrix, row, col, sign * determinant(sub));
			}
		}
		return comatrix;
	}
	
	//transposes given matrix
	public static Matrix transpose(Matrix matrix){
		if(matrix == null) return null;
		Matrix transpose = new Matrix(getWidth(matrix), getHeight(matrix));
		for(int mc = 0; mc < getHeight(transpose); mc++){ //for each column in matrix
			for(int mr = 0; mr < getWidth(transpose); mr++){ //for each row in matrix
				setCell(transpose, mc, mr, getCell(matrix, mr, mc));
			}
		}
		return transpose;
	}
	
	//helper function used to cross-out a row and column from a larger matrix and return the sub-matrix
	public static Matrix getSubMatrix(Matrix matrix, int row, int col){
		if(matrix == null) return null;
		if(getHeight(matrix) == 1 && getWidth(matrix) == 1) return matrix;  //matrix cannot be reduced
		Matrix sub = new Matrix(getHeight(matrix) - 1, getWidth(matrix) - 1);
		int mr = 0;
		int mc = 0;
		int sr = 0;
		int sc = 0;
		while(sr < getHeight(sub) && mr < getHeight(matrix)){ //while within row boundries
			if(mr == row){  //found row to skip
				mr++;
				continue;
			}
			while(sc < getWidth(sub) && mc < getWidth(matrix)){ //while within column boundries
				if(mc == col){ //found col to skip
					mc++;
					continue;
				}
				setCell(sub, sr, sc, getCell(matrix, mr, mc));
				sc++;
				mc++;
			}
			mc = 0;
			sc = 0;
			sr++;
			mr++;
		}
		return sub;
	}	

	public static Matrix getIdentityMatrix(int size, double scale){
		Matrix identity = new Matrix(size, size);
		for(int ir = 0; ir < getHeight(identity); ir++){
			for(int ic = 0; ic < getWidth(identity); ic++){
				setCell(identity, ir, ic, ir == ic ? scale : 0);
			}
		}
		return identity;
	}
	
	public static Matrix getIdentityMatrix(int size){
		Matrix identity = new Matrix(size, size);
		for(int ir = 0; ir < getHeight(identity); ir++){
			for(int ic = 0; ic < getWidth(identity); ic++){
				setCell(identity, ir, ic, ir == ic ? 1 : 0);
			}
		}
		return identity;
	}

	public static Matrix scale(Matrix matrix, double scale){
		Matrix scaled = new Matrix(getHeight(matrix), getWidth(matrix));
		for(int row = 0; row < getHeight(scaled); row++){
			for(int col = 0; col < getWidth(scaled); col++){
				setCell(scaled, row, col, scale * getCell(matrix, row, col));
			}
		}
		return scaled;
	}
	
	public static Matrix subtract(Matrix left, Matrix right){
		if(left == null) return null;
		if(right == null) return null;
		if(getHeight(left) != getHeight(right)) return null;
		if(getWidth(left) != getWidth(right)) return null;
		Matrix diff = new Matrix(getHeight(left), getWidth(left));
		
		for(int row = 0; row < getHeight(diff); row++){
			for(int col = 0; col < getWidth(diff); col++){
				setCell(diff, row, col, getCell(left, row, col) - getCell(right, row, col));
			}
		}
		return diff;
	}
	
	public static Matrix add(Matrix left, Matrix right){
		if(left == null) return null;
		if(right == null) return null;
		if(getHeight(left) != getHeight(right)) return null;
		if(getWidth(left) != getWidth(right)) return null;
		Matrix sum = new Matrix(getHeight(left), getWidth(left));
		
		for(int row = 0; row < getHeight(sum); row++){
			for(int col = 0; col < getWidth(sum); col++){
				setCell(sum, row, col, getCell(left, row, col) + getCell(right, row, col));
			}
		}
		return sum;
	}
	
	public static Matrix multiply(Matrix left, Matrix right){
		if(left == null) return null;
		if(right == null) return null;
		if(getWidth(left) != getHeight(right)) return null;
		
		Matrix result = new Matrix(getHeight(left), getWidth(right));
		for(int lr = 0; lr < getHeight(left); lr++){ //for each row in left
			for(int rc = 0; rc < getWidth(right); rc++){  //for each column in right
				double sum = 0;
				for(int lc = 0; lc < getWidth(left); lc++){ //for each column in left
						sum += getCell(left, lr, lc) * getCell(right, lc, rc); //calculate sum
				}
				setCell(result, lr, rc, sum);
			}
		}
		return result;
	}
	
	//calculates the determinant of a 3x3 matrix
	public static Double determinant3(Matrix matrix){
		if(matrix == null) return null;
		if(getHeight(matrix) != 3 && getWidth(matrix) != 3) return null;
		Double determinant = 0.;
		determinant += getCell(matrix, 0, 0) * getCell(matrix, 1, 1) * getCell(matrix, 2, 2);
		determinant += getCell(matrix, 0, 1) * getCell(matrix, 1, 2) * getCell(matrix, 2, 0);
		determinant += getCell(matrix, 0, 2) * getCell(matrix, 1, 0) * getCell(matrix, 2, 1);
		determinant += getCell(matrix, 2, 0) * getCell(matrix, 1, 1) * getCell(matrix, 0, 2);
		determinant += getCell(matrix, 2, 1) * getCell(matrix, 1, 2) * getCell(matrix, 0, 0);
		determinant += getCell(matrix, 2, 2) * getCell(matrix, 1, 0) * getCell(matrix, 0, 1);
		return determinant;
	}
	
	//calculates the determinant of a 2x2 matrix
	public static Double determinant2(Matrix matrix){
		if(matrix == null) return null;  //undefined
		if(getHeight(matrix) != 2 && getWidth(matrix) != 2) return null;
		if(getHeight(matrix) != getWidth(matrix)) return null;  //not square
		return (getCell(matrix, 0, 0) * getCell(matrix, 1, 1)) - (getCell(matrix, 1, 0) * getCell(matrix, 0, 1));
	}
	
	//calculates the inverse of a 2x2 matrix
	public static Matrix inverse2(Matrix matrix){
		if(matrix == null) return null;  //undefined
		if(getHeight(matrix) != getWidth(matrix)) return null;  //not square
		if(getHeight(matrix) != 2) return null;  //not a 2x2
		Matrix inverse = new Matrix(getHeight(matrix), getWidth(matrix));
		
		double determinant = 1/((getCell(matrix, 0, 0) * getCell(matrix, 1, 1)) - (getCell(matrix, 1, 0) * getCell(matrix, 0, 1)));
		setCell(inverse, 0, 0, getCell(matrix, 1, 1) * determinant);
		setCell(inverse, 1, 1, getCell(matrix, 0, 0) * determinant);
		setCell(inverse, 0, 1, getCell(matrix, 0, 1) * -determinant);
		setCell(inverse, 1, 0, getCell(matrix, 1, 0) * -determinant);
		return inverse;
	}
	
	//calculates general inverse
	public static Matrix inverse(Matrix matrix){
		if(matrix == null) return null;
		if(getHeight(matrix) != getWidth(matrix)) return null; //not square
		if(getHeight(matrix) == 2) return inverse2(matrix);
		Matrix cofactor = getCofactorMatrix(matrix);
		return scale(transpose(cofactor), 1/determinant(matrix, cofactor));
	}

	//calculates the determinant of a NxN matrix given its cofactor matrix
	public static Double determinant(Matrix matrix, Matrix cofactor){
		if(matrix != null && cofactor != null){
			if(getHeight(matrix) == getWidth(matrix) && getHeight(cofactor) == getWidth(cofactor) && getHeight(matrix) == getHeight(cofactor)){  //both are square
				double determinant = 0;
				for(int col = 0; col < getHeight(cofactor); col++){
					determinant += getCell(matrix, 0, col) * getCell(cofactor, 0, col);
				}
				return determinant;
			}
		}
		return null;
	}
	
	//recursively find determinant via cofactor expansion always choosing 1st row as expansion
	public static Double determinant(Matrix matrix) {
		  if(getHeight(matrix) != getWidth(matrix)) return null; //does not exist, matrix is not square
		  if(getHeight(matrix) == 3) return determinant3(matrix);  
		  if(getHeight(matrix) == 2) return determinant2(matrix);
		  double determinant = 0;
		  int sign = 1;     
		  for(int mc = 0; mc < getWidth(matrix); mc++) {
		    Matrix sub = getSubMatrix(matrix, 0, mc);
		    determinant += sign * getCell(matrix, 0, mc) * determinant(sub);
		    sign *= -1;
		  }
		  return determinant;
	} 

}
