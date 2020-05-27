//package com.company;

import java.util.*;
import java.io.File;
import java.io.*;

public class GO {

    /*Named constants start*/
    final static double KOMI = 2.5;
    final static int EMPTY = 0;
    final static int BLACK = 1;
    final static int WHITE = 2;
    /*Named constants end*/


    /* Instance variables start */
    int my_piece;
    int[][] previous_board;
    int[][] current_board;
    boolean did_opponent_pass;
    boolean is_first_move;
    int opp_piece;
    double my_PAS;
    double my_FAS;
    double opp_PAS;
    double opp_FAS;
    boolean flag_hs;
    boolean flag_vs;
    boolean flag_ds;
    boolean flag_ads;
    int moves_left;
    List<List<Integer>> valid_moves;
    List<List<Integer>> filtered_valid_moves;


    Map<List<Integer>, List<Integer>> horizontal_counterpart;
    Map<List<Integer>, List<Integer>> vertical_counterpart;
    Map<List<Integer>, List<Integer>> diagonal_counterpart;
    Map<List<Integer>, List<Integer>> anti_diagonal_counterpart;
    /* Instance variables end*/

    /*Initialization block starts*/
    {
        horizontal_counterpart = new HashMap<>();
        for(int i = 0; i < 5; i++){
            Integer[] arr1 = {0, i};
            Integer[] arr2 = {4, i};
            List<Integer> list1 = Arrays.asList(arr1);
            List<Integer> list2 = Arrays.asList(arr2);
            horizontal_counterpart.put(list1, list2);
            horizontal_counterpart.put(list2, list1);
            arr1 = new Integer[]{1, i};
            arr2 = new Integer[]{3, i};
            list1 = Arrays.asList(arr1);
            list2 = Arrays.asList(arr2);
            horizontal_counterpart.put(list1, list2);
            horizontal_counterpart.put(list2, list1);
        }

        vertical_counterpart = new HashMap<>();
        for(int i = 0; i < 5; i++){
            Integer[] arr1 = {i, 0};
            Integer[] arr2 = {i, 4};
            List<Integer> list1 = Arrays.asList(arr1);
            List<Integer> list2 = Arrays.asList(arr2);
            vertical_counterpart.put(list1, list2);
            vertical_counterpart.put(list2, list1);
            arr1 = new Integer[]{i, 1};
            arr2 = new Integer[]{i, 3};
            list1 = Arrays.asList(arr1);
            list2 = Arrays.asList(arr2);
            vertical_counterpart.put(list1, list2);
            vertical_counterpart.put(list2, list1);
        }

        diagonal_counterpart = new HashMap<>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(i != j) {
                    Integer[] arr1 = {i, j};
                    Integer[] arr2 = {j, i};
                    List<Integer> list1 = Arrays.asList(arr1);
                    List<Integer> list2 = Arrays.asList(arr2);
                    diagonal_counterpart.put(list1, list2);
                }
            }
        }

        anti_diagonal_counterpart = new HashMap<>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(i + j != 4) {
                    Integer[] arr1 = {i, j};
                    Integer[] arr2 = {4 - j, 4 - i};
                    List<Integer> list1 = Arrays.asList(arr1);
                    List<Integer> list2 = Arrays.asList(arr2);
                    anti_diagonal_counterpart.put(list1, list2);
                }
            }
        }

        my_PAS = 0.0;
        my_FAS = 0.0;
        opp_PAS = 0.0;
        opp_FAS = 0.0;
    }
    /*Initialization block ends*/


    /*Constructors start*/

    public GO() throws FileNotFoundException{
        read_input();
        this.did_opponent_pass = check_if_opponent_passed();
        this.is_first_move = check_if_first_move();
        this.opp_piece = 3 - this.my_piece;
        calculate_scores();
        this.valid_moves = get_valid_moves(this.my_piece, this.current_board, this.previous_board);
        this.flag_hs = is_horizontally_symmetric(this.current_board);
        this.flag_vs = is_vertically_symmetric(this.current_board);
        this.flag_ds = is_diagonally_symmetric(this.current_board);
        this.flag_ads = is_anti_diagonally_symmetric(this.current_board);
        this.filtered_valid_moves = new ArrayList<>(valid_moves);
        if(this.flag_hs){
            this.filtered_valid_moves = filter_horizontal_symmetry(this.filtered_valid_moves);
        }
        if(this.flag_vs){
            this. filtered_valid_moves = filter_vertical_symmetry(this.filtered_valid_moves);
        }
        if(this.flag_ds){
            this. filtered_valid_moves = filter_diagonal_symmetry(this.filtered_valid_moves);
        }
        if(this.flag_ads){
            this.filtered_valid_moves = filter_anti_diagonal_symmetry(this.filtered_valid_moves);
        }
        get_moves_left();
    }

    public GO(GO go, List<Integer> move) {
        this.my_piece = go.opp_piece;
        this.opp_piece = 3 - this.my_piece;
        this.previous_board = copy_board(go.current_board);
        this.current_board = copy_board(go.current_board);
        int i = move.get(0);
        int j = move.get(1);
        if(i != -1 && j != -1){
            current_board[i][j] = this.opp_piece;
            List<List<Integer>> dead_pieces = get_dead_pieces(this.my_piece, this.current_board);
            remove_dead_pieces(dead_pieces, this.current_board);
            this.did_opponent_pass = false;
        } else {
            this.did_opponent_pass = true;
        }
        this.is_first_move = check_if_first_move();
        calculate_scores();
        this.valid_moves = get_valid_moves(this.my_piece, this.current_board, this.previous_board);
        this.valid_moves = get_valid_moves(this.my_piece, this.current_board, this.previous_board);
        this.flag_hs = is_horizontally_symmetric(this.current_board);
        this.flag_vs = is_vertically_symmetric(this.current_board);
        this.flag_ds = is_diagonally_symmetric(this.current_board);
        this.flag_ads = is_anti_diagonally_symmetric(this.current_board);
        this.filtered_valid_moves = new ArrayList<>(valid_moves);
        if(this.flag_hs){
            this.filtered_valid_moves = filter_horizontal_symmetry(this.filtered_valid_moves);
        }
        if(this.flag_vs){
            this. filtered_valid_moves = filter_vertical_symmetry(this.filtered_valid_moves);
        }
        if(this.flag_ds){
            this. filtered_valid_moves = filter_diagonal_symmetry(this.filtered_valid_moves);
        }
        if(this.flag_ads){
            this.filtered_valid_moves = filter_anti_diagonal_symmetry(this.filtered_valid_moves);
        }
    }

    /*Constructors end*/

    public void calculate_scores(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(this.current_board[i][j] == this.my_piece){
                    this.my_PAS += 1.0;
                } else if(this.current_board[i][j] == this.opp_piece){
                    this.opp_PAS += 1.0;
                }
            }
        }
        this.my_FAS = this.my_PAS;
        this.opp_FAS = this.opp_PAS;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(this.current_board[i][j] == EMPTY){
                    Integer[] arr = {i, j};
                    List<Integer> position = Arrays.asList(arr);
                    List<List<Integer>> neighbors = get_neighbors(position);
                    boolean flag1 = false;
                    boolean flag2 = false;
                    for(List<Integer> neighbor : neighbors){
                        int i_nei = neighbor.get(0);
                        int j_nei = neighbor.get(1);
                        if(this.current_board[i_nei][j_nei] == this.my_piece && !flag1){
                            this.my_FAS += 1.0;
                            flag1 = true;
                        }
                        if(this.current_board[i_nei][j_nei] == this.opp_piece && !flag2){
                            this.opp_FAS += 1.0;
                            flag2 = true;
                        }
                        if(flag1 && flag2){
                            break;
                        }
                    }
                }
            }
        }
        if(this.my_piece == WHITE){
            this.my_PAS += KOMI;
            this.my_FAS += KOMI;
        } else {
            this.opp_PAS += KOMI;
            this.opp_FAS += KOMI;
        }
    }

    public boolean are_boards_equal(int[][] board1, int[][] board2){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void get_moves_left() throws FileNotFoundException {
        if(this.is_first_move ){
            if(this.my_piece == BLACK){
                this.moves_left = 24;
            } else {
                this.moves_left = 23;
            }
        } else {
            File inputFile = new File("moves.txt");
            Scanner in = new Scanner(inputFile);
            moves_left = in.nextInt();
        }
    }

    public void set_moves_left() throws FileNotFoundException {
        PrintWriter out = new PrintWriter("moves.txt");
        out.print(this.moves_left - 2);
        out.close();
    }

    public boolean is_board_empty(int[][] board1){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(board1[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean check_if_opponent_passed(){
        if(this.my_piece == BLACK && is_board_empty(this.previous_board) && is_board_empty(this.current_board)){
            return false;
        }
        return are_boards_equal(this.previous_board, this.current_board);
    }

    public boolean check_if_first_move(){
        if(this.my_piece == BLACK){
            return is_board_empty(this.previous_board) && is_board_empty(this.current_board);
        }
        return is_board_empty(this.previous_board);
    }

    public void read_input() throws FileNotFoundException{
        this.previous_board = new int[5][5];
        this.current_board = new int[5][5];
        File inputFile = new File("input.txt");
        Scanner in = new Scanner(inputFile);
        String line;
        line = in.nextLine();
        this.my_piece = Integer.parseInt(line);
        for(int i = 0; i < 5; i++){
            line = in.nextLine();
            for(int j = 0; j < 5; j++){
                this.previous_board[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
        for(int i = 0; i < 5; i++){
            line = in.nextLine();
            for(int j = 0; j < 5; j++){
                this.current_board[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
        in.close();
    }

    public void write_output(List<Integer> move) throws FileNotFoundException {
        PrintWriter out = new PrintWriter("output.txt");
        if(move.get(0) == -1){
            out.print("PASS");
        } else {
            out.print("" + move.get(0) + "," + move.get(1));
        }
        out.close();
    }

    public List<List<Integer>> get_neighbors(List<Integer> position){
        List<List<Integer>> neighbors = new ArrayList<>();
        int i = position.get(0);
        int j = position.get(1);
        if(i > 0){
            Integer[] arr = {i - 1, j};
            List<Integer> list = Arrays.asList(arr);
            neighbors.add(list);
        }
        if(i < 4){
            Integer[] arr = {i + 1, j};
            List<Integer> list = Arrays.asList(arr);
            neighbors.add(list);
        }
        if(j > 0){
            Integer[] arr = {i, j - 1};
            List<Integer> list = Arrays.asList(arr);
            neighbors.add(list);
        }
        if(j < 4){
            Integer[] arr = {i, j + 1};
            List<Integer> list = Arrays.asList(arr);
            neighbors.add(list);
        }
        //System.out.println("END: get_neighbors");
        return neighbors;
    }

    public List<List<Integer>> get_neighboring_allies(List<Integer> position, int[][]board){
        int i_pos = position.get(0);
        int j_pos = position.get(1);
        List<List<Integer>> neighbors = get_neighbors(position);
        List<List<Integer>> neighbor_allies = new ArrayList<>();
        for(List<Integer> neighbor : neighbors){
            int i_nei = neighbor.get(0);
            int j_nei = neighbor.get(1);
            if(board[i_pos][j_pos] == board[i_nei][j_nei]){
                Integer[] arr = {i_nei, j_nei};
                List<Integer> list = Arrays.asList(arr);
                neighbor_allies.add(list);
            }
        }
        //System.out.println("END: get_neighboring_allies - " + position[0] + " " + position[1] );
        return neighbor_allies;
    }

    public List<List<Integer>> find_ally_group(List<Integer> position, int[][]board){
        Queue<List<Integer>> queue = new LinkedList<>();
        ArrayList<List<Integer>> ally_group = new ArrayList<>();
        Integer[] arr = {position.get(0), position.get(1)};
        List<Integer> list = Arrays.asList(arr);
        queue.add(list);
        Set<List<Integer>> visited = new HashSet<>();
        visited.add(list);
        while (!queue.isEmpty()){
            List<Integer> piece = queue.remove();
            ally_group.add(piece);
            visited.add(piece);
            List<List<Integer>> neighbor_allies = get_neighboring_allies(piece, board);
            for(List<Integer> neighbor_ally : neighbor_allies){
                /*boolean flag = false;
                for(int[] ele : visited){
                    if(Arrays.equals(neighbor_ally, ele)){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    queue.add(neighbor_ally);
                }*/
                if(!visited.contains(neighbor_ally) && !queue.contains(neighbor_ally)){
                    queue.add(neighbor_ally);
                }
            }
        }
        //System.out.println("END: find_ally_group - " + position[0] + " " + position[1] );
        return ally_group;
    }

    public boolean has_liberty(List<Integer> position, int[][] board){
        List<List<Integer>> allies = find_ally_group(position, board);
        for(List<Integer> ally : allies){
            List<List<Integer>> neighbors = get_neighbors(ally);
            for (List<Integer> neighbor  : neighbors){
                if(board[neighbor.get(0)][neighbor.get(1)] == EMPTY){
                    //System.out.println("END: has_liberty - true");
                    return true;
                }
            }
        }
        //System.out.println("END: has_liberty - false");
        return false;
    }

    public List<List<Integer>> get_dead_pieces(int piece_type, int[][] board){
        List<List<Integer>> dead_pieces = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(board[i][j] == piece_type){
                    Integer[] arr = {i, j};
                    List<Integer> position = Arrays.asList(arr);
                    if(!has_liberty(position, board)){
                        dead_pieces.add(position);
                    }
                }
            }
        }
        //System.out.println("END: get_dead_pieces");
        return dead_pieces;
    }

    public boolean remove_dead_pieces( List<List<Integer>> dead_pieces, int[][] board){
        if(dead_pieces.isEmpty()){
            return false;
        }
        for(List<Integer> piece : dead_pieces){
            board[piece.get(0)][piece.get(1)] = EMPTY;
        }
        //System.out.println("END: remove_dead_pieces");
        return true;
    }

    public int[][] copy_board(int [][] board){
        int[][] copy = new int[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public List<List<Integer>> get_valid_moves(int piece_type, int[][] board, int[][] prev){
        List<List<Integer>> valid_moves = new ArrayList<>();
        Integer[] pass = {-1, -1};
        List<Integer> pass_list = Arrays.asList(pass);
        valid_moves.add(pass_list);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(board[i][j] == EMPTY){
                    int[][] test_board = copy_board(board);
                    test_board[i][j] = piece_type;
                    Integer[] test_pos_arr = {i, j};
                    List<Integer> test_pos = Arrays.asList(test_pos_arr);
                    if(has_liberty(test_pos, test_board)){
                        valid_moves.add(test_pos);
                        //System.out.println("" + i + "," + j);
                        continue;
                    }
                    List<List<Integer>> dead_pieces = get_dead_pieces(3 - piece_type, test_board);
                    boolean dead_removed = remove_dead_pieces(dead_pieces, test_board);
                    if(has_liberty(test_pos, test_board)){
                        if(!dead_removed || !are_boards_equal(prev, test_board)){
                            valid_moves.add(test_pos);
                        }
                    }
                }
                //System.out.println("" + i + "," + j);
            }
        }
        return valid_moves;
    }

    public boolean is_horizontally_symmetric(int[][] board){
        for(int i = 0; i < 5; i++){
            if(board[0][i] != board[4][i]){
                return false;
            }
            if(board[1][i] != board[3][i]) {
                return false;
            }
        }
        return true;
    }

    public boolean is_vertically_symmetric(int[][] board){
        for(int i = 0; i < 5; i++){
            if(board[i][0] != board[i][4]){
                return false;
            }
            if(board[i][1] != board[i][3]) {
                return false;
            }
        }
        return true;
    }

    public boolean is_diagonally_symmetric(int[][] board){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < i; j++){
                if(board[i][j] != board[j][i]){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean is_anti_diagonally_symmetric(int[][] board){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 4 - i; j++){
                if(board[i][j] != board[4 - j][4 - i]){
                    return false;
                }
            }
        }
        return true;
    }

    public List<List<Integer>> filter_horizontal_symmetry(List<List<Integer>> valid_moves){
        Set<List<Integer>> set = new HashSet<>(valid_moves);
        for(int i = 0; i < 5; i++){
            Integer[] arr1 = {0, i};
            Integer[] arr2 = {1, i};
            List<Integer> list1 = Arrays.asList(arr1);
            List<Integer> list2 = Arrays.asList(arr2);
            List<Integer> list1c = this.horizontal_counterpart.get(list1);
            List<Integer> list2c = this.horizontal_counterpart.get(list2);
            if(set.contains(list1)){
                set.remove(list1c);
            }
            if(set.contains(list2)){
                set.remove(list2c);
            }
        }
        return new ArrayList<>(set);
    }

    public List<List<Integer>> filter_vertical_symmetry(List<List<Integer>> valid_moves){
        Set<List<Integer>> set = new HashSet<>(valid_moves);
        for(int i = 0; i < 5; i++){
            Integer[] arr1 = {i, 0};
            Integer[] arr2 = {i, 1};
            List<Integer> list1 = Arrays.asList(arr1);
            List<Integer> list2 = Arrays.asList(arr2);
            List<Integer> list1c = this.vertical_counterpart.get(list1);
            List<Integer> list2c = this.vertical_counterpart.get(list2);
            if(set.contains(list1)){
                set.remove(list1c);
            }
            if(set.contains(list2)){
                set.remove(list2c);
            }
        }
        return new ArrayList<>(set);
    }

    public List<List<Integer>> filter_diagonal_symmetry(List<List<Integer>> valid_moves){
        Set<List<Integer>> set = new HashSet<>(valid_moves);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < i; j++){
                Integer[] arr1 = {i, j};
                List<Integer> list1 = Arrays.asList(arr1);
                List<Integer> list1c = this.diagonal_counterpart.get(list1);
                if(set.contains(list1)){
                    set.remove(list1c);
                }
            }
        }
        return new ArrayList<>(set);
    }

    public List<List<Integer>> filter_anti_diagonal_symmetry(List<List<Integer>> valid_moves){
        Set<List<Integer>> set = new HashSet<>(valid_moves);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 4 - i; j++){
                Integer[] arr1 = {i, j};
                List<Integer> list1 = Arrays.asList(arr1);
                List<Integer> list1c = this.anti_diagonal_counterpart.get(list1);
                if(set.contains(list1)){
                    set.remove(list1c);
                }
            }
        }
        return new ArrayList<>(set);
    }

}


