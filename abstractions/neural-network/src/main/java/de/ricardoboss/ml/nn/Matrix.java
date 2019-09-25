package de.ricardoboss.ml.nn;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class Matrix {
    public interface Function {
        double apply(double value);
    }

    public static Matrix random(int rows, int cols)
    {
        var m = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                m.data[i][j] = Math.random();

        return m;
    }

    public final int rows;
    public final int cols;
    private final double[][] data;

    public Matrix(int rows, int cols)
    {
        if (rows < 1 || cols < 1)
            throw new IllegalArgumentException("Cannot create matrix: number of columns and rows must be greater than .!");

        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data)
    {
        if (data.length == 0 || data[0].length == 0)
            throw new IllegalArgumentException("Cannot create matrix: must have at least one row and column.");

        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;

        for (double[] row : data) {
            if (row.length != this.cols)
                throw new IllegalArgumentException("Cannot create matrix: given data has an inconsistent number of columns.");
        }
    }

    public double get(int x, int y)
    {
        return this.data[x][y];
    }

    public Matrix set(int x, int y, double val)
    {
        this.data[x][y] = val;

        return this;
    }

    public Matrix apply(Function f)
    {
        var r = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r.data[i][j] = f.apply(data[i][j]);
            }
        }

        return r;
    }

    public Matrix dot(Matrix m)
    {
        var a = this;
        var b = m;
        if (a.cols != b.rows && a.rows == b.cols) {
            // switch matrices
            var c = b;
            b = a;
            a = c;
        } else if (a.cols != b.rows) {
            throw new IllegalArgumentException("Cannot apply dot product: number of columns and rows doesn't match!");
        }

        var r = new Matrix(a.rows, b.cols);
        for (int i = 0; i < a.rows; i++)
        {
            for (int j = 0; j < b.cols; j++)
            {
                double s = 0;

                for (var k = 0; k < a.cols; k++)
                    s += a.data[i][k] * b.data[k][j];

                r.data[i][j] = s;
            }
        }

        return r;
    }

    public Matrix sub(Matrix m)
    {
        if (this.rows != m.rows || this.cols != m.cols)
            throw new IllegalArgumentException("Cannot subtract matrices: number of rows and columns doesn't match up!");

        var r = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r.data[i][j] = this.data[i][j] - m.data[i][j];

        return r;
    }

    public Matrix sumCols()
    {
        var r = new Matrix(1, cols);
        for (int i = 0; i < cols; i++)
        {
            var s = 0d;

            for (int j = 0; j < rows; j++)
                s += data[j][i];

            r.data[0][i] = s;
        }

        return r;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Matrix clone()
    {
        var copy = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++)
            System.arraycopy(
                    this.data[i], 0,
                    copy.data[i], 0,
                    cols
            );

        return copy;
    }

    @Override
    public String toString() {
        return this.toString(5);
    }

    public String toString(int precision) {
        final char cornerTopLeft = '\u250C', cornerTopRight = '\u2510', cornerBottomLeft = '\u2514',
                cornerBottomRight = '\u2518', lineVertical = '\u2502';

        var sb = new StringBuilder();

        var longestLine = 0;
        for (int i = 0; i < rows; i++)
        {
            sb.append(lineVertical).append(" ");
            var lineLength = 2;

            for (int j = 0; j < cols; j++)
            {
                var line = String.format("%." + precision + "f", data[i][j]).substring(0, 7);
                sb.append(line);

                lineLength += line.length();

                if (j < cols - 1) {
                    sb.append("   ");
                    lineLength += 3;
                }
            }

            sb.append(" ").append(lineVertical).append(System.lineSeparator());
            lineLength += 2;

            if (longestLine < lineLength)
                longestLine = lineLength;
        }

        var space = new String(new char[longestLine - 2]).replace('\0', ' ');
        return cornerTopLeft +
                space +
                cornerTopRight +
                System.lineSeparator() +
                sb.toString() +
                cornerBottomLeft +
                space +
                cornerBottomRight +
                System.lineSeparator();
    }
}
