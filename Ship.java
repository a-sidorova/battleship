import java.util.ArrayList;

public class Ship {
    int rank = -1;
    char orient = 'n';
    ArrayList<Point> coords = new ArrayList<Point>();

    public Ship(int rank_, char oriented_, Point p) {
        rank = rank_;
        orient = oriented_;
        build(p);
    }

    private void build(Point p) {
        int h = orient == 'h' ? 1 : 0;

        for (int i = 0; i < rank; i++) {
            Point c = new Point(p.x + i * (1 - h), p.y + i * h);
            coords.add(c);
        }
    }

    static int getRank(int num) {
        int rank = 0;
        if (num < 4) {
            rank = 1;
        } else if (num < 7) {
            rank = 2;
        } else if (num < 9) {
            rank = 3;
        } else if (num < 10) {
            rank = 4;
        }
        return rank;
    }

    public boolean hasPoint(Point p) {
        for (Point c : coords) {
            if (c.x == p.x && c.y == p.y)
                return true;
        }

        return false;
    }
}