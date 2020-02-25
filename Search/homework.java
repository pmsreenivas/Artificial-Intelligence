package com.company;

import java.io.PrintWriter;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.time.*;

public class homework {

    public static void main(String[] args) throws FileNotFoundException {

        Instant start = Instant.now();

        File inputFile = new File("input.txt");
        Scanner in = new Scanner(inputFile);
        String method = in.nextLine();
        in.close();

        if(method.equals("BFS")) BFS();
        else if(method.equals("UCS")) UCS();
        else if(method.equals("A*")) A_Star();

        Instant finish = Instant.now();

        long timeElapsed = Duration.between(start, finish).getSeconds();

        System.out.println(timeElapsed);

    }

    private static void BFS () throws FileNotFoundException{
        year_location source = new homework().new year_location(2020, 17, 24, 0, 0);
        year_location dest = new homework().new year_location(2020, 17, 24, 0, 0);
        int width, height;
        Map<year_location, ArrayList<year_location>> channels = new HashMap<year_location, ArrayList<year_location>>();
        int[] dims = parseInputFile(source, dest, channels);
        width = dims[0];
        height = dims[1];
       /*System.out.printf("We are in year %d and location (%d, %d)\n", source.year, source.x, source.y);
        System.out.printf("We need to reach year %d and location (%d, %d)\n", dest.year, dest.x, dest.y);
        System.out.printf("The width and height of the grids are %d and %d respectively\n", width, height);
        System.out.println("The following are channels:-");
        for(year_location key : channels.keySet()){
            ArrayList<year_location> list = channels.get(key);
            for(year_location yl : list){
                System.out.println("" + key.year + " (" + key.x + ", " + key.y + ") " + yl.year);
            }
        }*/
        Queue<year_location> frontier = new LinkedList<year_location>();
        Set<year_location> explored = new HashSet<year_location>();
        frontier.add(source);
        boolean goal_found = false;
        year_location child = source;
        if(child.equals(dest)){
            goal_found = true;
            frontier.poll();
        }
        search: while(!frontier.isEmpty()){
            year_location curLoc = frontier.poll();
            explored.add(curLoc);
            //System.out.println(curLoc);
            int rowMin = Math.max(curLoc.x - 1, 0);
            int colMin = Math.max(curLoc.y - 1, 0);
            int rowMax = Math.min(curLoc.x + 1, width - 1);
            int colMax = Math.min(curLoc.y + 1, height - 1);
            for(int x = rowMin; x <= rowMax; x++){
                for(int y = colMin; y <= colMax; y++){
                    if(x != curLoc.x || y != curLoc.y) {
                        child = new homework().new year_location(curLoc.year, x, y, curLoc.past_cost + 1, 0);
                        child.parent = curLoc;
                        if (!explored.contains(child) && !frontier.contains(child)){
                            if (child.equals(dest)) {
                                goal_found = true;
                                break search;
                            } else {
                                frontier.add(child);
                            }
                        }
                    }
                }
            }
            if(channels.containsKey(curLoc)){
                ArrayList<year_location> list = channels.get(curLoc);
                for(year_location end : list){
                    child = new homework().new year_location(end.year, curLoc.x, curLoc.y, curLoc.past_cost + 1, 0);
                    child.parent = curLoc;
                    if (!explored.contains(child) && !frontier.contains(child)){
                        if (child.equals(dest)) {
                            goal_found = true;
                            break search;
                        } else {
                            frontier.add(child);
                        }
                    }
                }
            }
        }
        if(!goal_found){
            handle_failure();
            return;
        }
        long total_cost = child.past_cost;
        ArrayList<year_location> path = new ArrayList<year_location>();
        path.add(child);
        year_location curr = child.parent;
        while (curr != null){
            path.add(curr);
            curr = curr.parent;
        }
        Collections.reverse(path);
        long num_steps = path.size();
        PrintWriter out = new PrintWriter("output.txt");
        out.println(total_cost);
        out.println(num_steps);
        long prev, cost, diff;
        prev = cost = diff = 0;
        int i = 0;
        for(i = 0; i < path.size() - 1; i++){
            year_location curLoc = path.get(i);
            cost = curLoc.past_cost;
            diff = cost - prev;
            String str = "" + curLoc.year + " " + curLoc.x + " " + curLoc.y + " " + diff;
            prev = cost;
            out.println(str);
        }
        curr = path.get(i);
        cost = curr.past_cost;
        diff = cost - prev;
        String str = "" + curr.year + " " + curr.x + " " + curr.y + " " + diff;
        out.print(str);
        out.close();
    }

    private static void UCS () throws FileNotFoundException{
        year_location source = new homework().new year_location(2020, 17, 24, 0, 0);
        year_location dest = new homework().new year_location(2020, 17, 24, 0, 0);
        int width, height;
        Map<year_location, ArrayList<year_location>> channels = new HashMap<year_location, ArrayList<year_location>>();
        int[] dims = parseInputFile(source, dest, channels);
        width = dims[0];
        height = dims[1];
       /*System.out.printf("We are in year %d and location (%d, %d)\n", source.year, source.x, source.y);
        System.out.printf("We need to reach year %d and location (%d, %d)\n", dest.year, dest.x, dest.y);
        System.out.printf("The width and height of the grids are %d and %d respectively\n", width, height);
        System.out.println("The following are channels:-");
        for(year_location key : channels.keySet()){
            ArrayList<year_location> list = channels.get(key);
            for(year_location yl : list){
                System.out.println("" + key.year + " (" + key.x + ", " + key.y + ") " + yl.year);
            }
        }*/
        PriorityQueue<year_location> frontier = new PriorityQueue<year_location>();
        Set<year_location> explored = new HashSet<year_location>();
        frontier.add(source);
        boolean goal_found = false;
        year_location child = source;
        if(child.equals(dest)){
            goal_found = true;
            frontier.poll();
        }
        while(!frontier.isEmpty()){
            year_location curLoc = frontier.poll();
            //System.out.println(curLoc + "---" + curLoc.past_cost);
            if(curLoc.equals(dest)){
                goal_found = true;
                child = curLoc;
                break;
            }
            if(explored.contains(curLoc)){
                continue;
            }
            explored.add(curLoc);
            int rowMin = Math.max(curLoc.x - 1, 0);
            int colMin = Math.max(curLoc.y - 1, 0);
            int rowMax = Math.min(curLoc.x + 1, width - 1);
            int colMax = Math.min(curLoc.y + 1, height - 1);
            for(int x = rowMin; x <= rowMax; x++){
                for(int y = colMin; y <= colMax; y++){
                    if(x != curLoc.x || y != curLoc.y) {
                        int cost;
                        cost = x == curLoc.x || y == curLoc.y ? 10 : 14;
                        child = new homework().new year_location(curLoc.year, x, y, curLoc.past_cost + cost, 0);
                        child.parent = curLoc;
                        if(!explored.contains(child)){
                           frontier.add(child);
                        }
                    }
                }
            }
            if(channels.containsKey(curLoc)){
                ArrayList<year_location> list = channels.get(curLoc);
                for(year_location end : list){
                    child = new homework().new year_location(end.year, curLoc.x, curLoc.y, curLoc.past_cost + Math.abs(end.year - curLoc.year), 0);
                    child.parent = curLoc;
                    if(!explored.contains(child)){
                        frontier.add(child);
                    }
                }
            }
        }
        if(!goal_found){
            handle_failure();
            return;
        }
        long total_cost = child.past_cost;
        ArrayList<year_location> path = new ArrayList<year_location>();
        path.add(child);
        year_location curr = child.parent;
        while (curr != null){
            path.add(curr);
            curr = curr.parent;
        }
        Collections.reverse(path);
        long num_steps = path.size();
        PrintWriter out = new PrintWriter("output.txt");
        out.println(total_cost);
        out.println(num_steps);
        long prev, cost, diff;
        prev = cost = diff = 0;
        int i = 0;
        for(i = 0; i < path.size() - 1; i++){
            year_location curLoc = path.get(i);
            cost = curLoc.past_cost;
            diff = cost - prev;
            String str = "" + curLoc.year + " " + curLoc.x + " " + curLoc.y + " " + diff;
            prev = cost;
            out.println(str);
        }
        curr = path.get(i);
        cost = curr.past_cost;
        diff = cost - prev;
        String str = "" + curr.year + " " + curr.x + " " + curr.y + " " + diff;
        out.print(str);
        out.close();
    }

    private static void A_Star()throws FileNotFoundException{
        year_location source = new homework().new year_location(2020, 17, 24, 0, 0);
        year_location dest = new homework().new year_location(2020, 17, 24, 0, 0);
        int width, height;
        Map<year_location, ArrayList<year_location>> channels = new HashMap<year_location, ArrayList<year_location>>();
        int[] dims = parseInputFile(source, dest, channels);
        width = dims[0];
        height = dims[1];
       /*System.out.printf("We are in year %d and location (%d, %d)\n", source.year, source.x, source.y);
        System.out.printf("We need to reach year %d and location (%d, %d)\n", dest.year, dest.x, dest.y);
        System.out.printf("The width and height of the grids are %d and %d respectively\n", width, height);
        System.out.println("The following are channels:-");
        for(year_location key : channels.keySet()){
            ArrayList<year_location> list = channels.get(key);
            for(year_location yl : list){
                System.out.println("" + key.year + " (" + key.x + ", " + key.y + ") " + yl.year);
            }
        }*/
        PriorityQueue<year_location> frontier = new PriorityQueue<year_location>();
        Set<year_location> explored = new HashSet<year_location>();
        /*long heuristic2;
        int year_diff2 = Math.abs(source.year - dest.year);
        int z2 = (dest.x - source.x) * (dest.x - source.x);
        int zz2 = (dest.y - source.y) * (dest.y - source.y);
        double zzz2 = Math.sqrt(z2 + zz2);
        int zzzz2 = (int) Math.floor(zzz2);
        heuristic2 = year_diff2 + 10*zzzz2;*/
        source.est_fut_cost = get_heuristic(source.x, dest.x, source.y, dest.y, source.year, dest.year);
        frontier.add(source);
        boolean goal_found = false;
        year_location child = source;
        if(child.equals(dest)){
            goal_found = true;
            frontier.poll();
        }
        while(!frontier.isEmpty()){
            year_location curLoc = frontier.poll();
            //long total = curLoc.past_cost + curLoc.est_fut_cost;
            //System.out.println(curLoc + "---" + curLoc.past_cost + "---" + curLoc.est_fut_cost + "---" + total);
            if(curLoc.equals(dest)){
                goal_found = true;
                child = curLoc;
                break;
            }
            if(explored.contains(curLoc)){
                continue;
            }
            explored.add(curLoc);
            int rowMin = Math.max(curLoc.x - 1, 0);
            int colMin = Math.max(curLoc.y - 1, 0);
            int rowMax = Math.min(curLoc.x + 1, width - 1);
            int colMax = Math.min(curLoc.y + 1, height - 1);
            for(int x = rowMin; x <= rowMax; x++){
                for(int y = colMin; y <= colMax; y++){
                    if(x != curLoc.x || y != curLoc.y) {
                        int cost;
                        cost = x == curLoc.x || y == curLoc.y ? 10 : 14;
                        long heuristic;
                        /*int year_diff = Math.abs(curLoc.year - dest.year);
                        int z = (dest.x - x) * (dest.x - x);
                        int zz = (dest.y - y) * (dest.y - y);
                        double zzz = Math.sqrt(z + zz);
                        int zzzz = (int) Math.floor(zzz);*/
                        heuristic = get_heuristic(dest.x, x, dest.y, y, dest.year, curLoc.year);
                        child = new homework().new year_location(curLoc.year, x, y, curLoc.past_cost + cost, heuristic);
                        child.parent = curLoc;
                        if(!explored.contains(child)){
                            frontier.add(child);
                        }
                    }
                }
            }
            if(channels.containsKey(curLoc)){
                ArrayList<year_location> list = channels.get(curLoc);
                for(year_location end : list){
                    long heuristic;
                    /*int year_diff = Math.abs(end.year - dest.year);
                    int z = (dest.x - curLoc.x) * (dest.x - curLoc.x);
                    int zz = (dest.y - curLoc.y) * (dest.y - curLoc.y);
                    double zzz = Math.sqrt(z + zz);
                    int zzzz = (int) Math.floor(zzz);*/
                    heuristic = get_heuristic(dest.x, curLoc.x, dest.y, curLoc.y, end.year, dest.year);
                    child = new homework().new year_location(end.year, curLoc.x, curLoc.y, curLoc.past_cost + Math.abs(end.year - curLoc.year), heuristic);
                    child.parent = curLoc;
                    if(!explored.contains(child)){
                        frontier.add(child);
                    }
                }
            }
        }
        if(!goal_found){
            handle_failure();
            return;
        }
        long total_cost = child.past_cost;
        ArrayList<year_location> path = new ArrayList<year_location>();
        path.add(child);
        year_location curr = child.parent;
        while (curr != null){
            path.add(curr);
            curr = curr.parent;
        }
        Collections.reverse(path);
        long num_steps = path.size();
        PrintWriter out = new PrintWriter("output.txt");
        out.println(total_cost);
        out.println(num_steps);
        long prev, cost, diff;
        prev = cost = diff = 0;
        int i = 0;
        for(i = 0; i < path.size() - 1; i++){
            year_location curLoc = path.get(i);
            cost = curLoc.past_cost;
            diff = cost - prev;
            String str = "" + curLoc.year + " " + curLoc.x + " " + curLoc.y + " " + diff;
            prev = cost;
            out.println(str);
        }
        curr = path.get(i);
        cost = curr.past_cost;
        diff = cost - prev;
        String str = "" + curr.year + " " + curr.x + " " + curr.y + " " + diff;
        out.print(str);
        out.close();
    }

    class year_location implements Comparable {

        int year;
        int x;
        int y;
        long past_cost;
        long est_fut_cost;
        year_location parent;

        year_location(int year, int x, int y, long past_cost, long est_fut_cost){
            this.year = year;
            this.x = x;
            this.y = y;
            this.past_cost = past_cost;
            this.est_fut_cost = est_fut_cost;
            this.parent = null;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) return true;
            if(!(o instanceof year_location)) return false;
            year_location d = (year_location) o;
            return this.year == d.year && this.x == d.x && this.y == d.y;
        }

        @Override
        public int hashCode(){
            if(this == null) return 0;
            return 29*this.year + 17*((int)Math.pow(this.x, 2)) + 13*((int)Math.pow(this.y, 3));
        }

        @Override
        public String toString(){
            if(this == null) return "null";
            return "" + this.year + "-(" + this.x + ", " + this.y + ")";
        }

        public int compareTo(Object otherObj){
            year_location other = (year_location) otherObj;
            return (int)((this.past_cost + this.est_fut_cost) - (other.past_cost + other.est_fut_cost));
        }

    }

    private static int[] parseInputFile (year_location source, year_location dest, Map<year_location, ArrayList<year_location>> channels) throws FileNotFoundException{
        File inputFile = new File("input.txt");
        Scanner in = new Scanner(inputFile);
        String method = in.nextLine();
        String line = in.nextLine();
        Scanner lineScanner = new Scanner(line);
        int [] dims = new int[2];
        dims[0] = lineScanner.nextInt();
        dims[1] = lineScanner.nextInt();
        line = in.nextLine();
        lineScanner = new Scanner(line);
        source.year = lineScanner.nextInt();
        source.x = lineScanner.nextInt();
        source.y = lineScanner.nextInt();
        line = in.nextLine();
        lineScanner = new Scanner(line);
        dest.year = lineScanner.nextInt();
        dest.x = lineScanner.nextInt();
        dest.y = lineScanner.nextInt();
        if(!in.hasNextLine()) {
            in.close();
            return dims;
        }
        line = in.nextLine();
        lineScanner = new Scanner(line);
        int num_channels = lineScanner.nextInt();
        while(in.hasNextLine()){
            line = in.nextLine();
            lineScanner = new Scanner(line);
            int year_1 = lineScanner.nextInt();
            int x = lineScanner.nextInt();
            int y = lineScanner.nextInt();
            int year_2 = lineScanner.nextInt();
            if(year_1 == year_2) continue;
            year_location left = new homework().new year_location(year_1, x, y, 0, 0);
            year_location right = new homework().new year_location(year_2, x, y, 0, 0);
            ArrayList<year_location> list = channels.getOrDefault(left, new ArrayList<year_location>());
            list.add(right);
            channels.put(left, list);
            list = channels.getOrDefault(right, new ArrayList<year_location>());
            list.add(left);
            channels.put(right, list);
        }
        in.close();
        return dims;
    }

    private static void handle_failure() throws  FileNotFoundException{
        PrintWriter out = new PrintWriter("output.txt");
        out.print("FAIL");
        out.close();
    }

    private static long get_heuristic(int x1, int x2, int y1, int y2, int year1, int year2){
        long dx = (long)Math.abs(x1 - x2);
        long dy = (long)Math.abs(y1 - y2);
        long min = Math.min(dx, dy);
        long max = Math.max(dx, dy);
        return 10 * max + 4 * min + (long)Math.abs(year1 - year2);
    }

}
