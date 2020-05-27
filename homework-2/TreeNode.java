//package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNode implements Comparable<TreeNode>{

    /*Instance variables start*/
    final static int MAX_CHILDREN = 15;

    TreeNode parent;
    GO go;
    double v;
    //double alpha;
    //double beta;
    List<TreeNode> children;
    boolean isMax;
    int depth;
    List<Integer> best_child_move;
    List<Integer> parent_move;
    int last_explored;
    boolean children_generated;
    /*Instance variables end*/

    public TreeNode(TreeNode parent, GO go, boolean isMax, int depth, List<Integer> parent_move){
        this.parent = parent;
        this.go = go;
        this.isMax = isMax;
        this.depth = depth;
        //this.alpha = Double.MIN_VALUE;
        //this.beta = Double.MAX_VALUE;
        this.v = isMax ? -10000.0 : 10000.0;
        this.parent_move = parent_move;
        last_explored = -1;
        children_generated = false;
    }

    public void generate_children(){
        children = new ArrayList<>();
        for (List<Integer> move : this.go.filtered_valid_moves){
            GO child_go = new GO(this.go, move);
            TreeNode child = new TreeNode(this, child_go, !this.isMax, this.depth + 1, move);
            children.add(child);
        }
        children.sort(Collections.reverseOrder());
        if(children.size() > MAX_CHILDREN){
            children = children.subList(0, MAX_CHILDREN);
        }
        children_generated = true;
    }

    public int compareTo(TreeNode other){
        return (int)((this.go.opp_FAS - this.go.my_FAS) - (other.go.opp_FAS - other.go.my_FAS));
    }

}


