//author: Renaldo Hyacinthe
//this is essentially a program that takes an input of numbers that start with from the command line that are between
//0-99 and creates a maze based on each input
//in the maze one can go forwards or backwards based on
//the integer size of the particular input
//the algorithim finds and displays all solutions to each
//@version 2 - 11/16/2021
import java.util.ArrayList;
public class WayFinder {
    WayFinder()
    {}
    Node node;
    ArrayList<Node> solutionlist= new ArrayList<Node>();
    //this is an ArrayList that I add each solution that is found to


    //this method is so that I can set the value of the node field within my WayFinder object instantiation
    public Node setNode(int index, char direction)
    {
        return new Node(index, direction);
    }
    //this is my algorithm
    //it takes an array input, a position input, a boolean array input, and a linked list Node
    //its purpose is to take an input of numbers and find all possible ways to reach its final input 0

    public void solver(int[] array, int position, boolean[] traveled, Node originalhead)
    {
        //the integer array input consists of the literal values that are inputted by the users
        //i use it so that I can traverse the array
        //the traveled array holds a boolean that stops or continues the algorithm
        //based on whether or not that input has been traversed
        if (traveled[position])
            //if the position has been traveled then the path is deemed incorrect
            return;
        //if the position is equal to the final position of the array, then we have found an input!
        //here we add a solution to the ArrayList of solutions and end the path
        if (position == array.length-1)
        {

            
            solutionlist.add(originalhead.cloner());
            //

            return;
        }
        //if the position is invalid then we deem the path invalid
        if (position < 0 || position > array.length-1)
        {
            return;
        }
            //if a move is so large that it cannot move forward or backwards, then the path ends because
        //this value is of no help to me
        if ((position + array[position] > array.length-1) 
            && (position - array[position] < 0))
            return;


        //after checking if a move is valid we can move forward
        if (position + array[position] <= array.length-1)
        {
            //this statement is the recursive move forward
            //here we are making a clone of traveled because
            //i want to store the direction of the move i am making
            //and if alter the original array, then the potential move backwards will be affected

            boolean[] travelclone = new boolean[traveled.length];
            for (int z = 0; z < travelclone.length; z++)
                travelclone[z] = traveled[z];
            travelclone[position] = true;
            ////here i am creating a clone of originalhead - which stores all previous moves from the algorithm
            //i want a clone because if i do not create a new object, then potentially incorrect paths, would point to
            //the original object, even if they had already been deemed incorrect
            Node head = originalhead.cloner();
            Node newer =  new Node(position, 'R');
            head.add(newer);
            //

//                //here i am checking if this is the first iteration
            //if so then i will use the original reference because there is no possible
            //way we could go backwards

            if (position == 0)
                solver(array, position + array[position], travelclone, 
                        originalhead);
            else
            {
            solver(array, position + array[position], travelclone, head);
            }
        }
        if (position - array[position] >= 0)
        {
            //this statement is the recursive move backwards
            //here i am doing pretty much the same thing i did in the previous case
            //but at the end we go backwards (subtract)
            boolean[] travelclone = new boolean[traveled.length];
            for (int z = 0; z < travelclone.length; z++)
                travelclone[z] = traveled[z];
            travelclone[position] = true;
            Node head = originalhead.cloner();
            Node newer = new Node(position, 'L');
            head.add(newer);
            solver(array, position - array[position], travelclone, head);
        }
    }

    //this function prints all the solutions that were found in the problem in a formatted manner
    //firstly it arranges formats the numbers of every inputted number
    //then it goes through each solution list, and prints each move line by line, along with all the other elements
    //formatted correctly
    public void printer(ArrayList<Node> solutionlist, int[] array)
    {
        String move="";
        String total="";
        String[] formatted = new String[array.length];
        for (int i = 0; i < array.length; i++)
        {
            formatted[i] = String.format("%2d%c", array[i],' ');
            
        }
        
        for (int y =0; y<solutionlist.size(); y++)
        {
            Node current = solutionlist.get(y);
            move = String.format("%2s%c", array[current.index],current.direction);
            
            while (current !=null)
            {
                for (int z = 0; z <array.length;z++)
                {
                    if (z==0)
                       total+= "[";
                    if (z!=array.length-1)
                    {
                        if (z!=current.index)
                            total += formatted[z] + ", ";
                        else 
                            total += move + ", " ;
                    }
                    else
                    {
                        total += formatted[z];
                        total += "]";
                    }
                }
                current = current.next;
                if (current !=null)
                    move = String.format("%2s%c", array[current.index],current.direction);
                System.out.println(total);
                total="";
                
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        int[] array = new int[args.length];
        try
        {
            //here I am parsing the ints to get an integer array
            //if it doesnt work then i know that I have an invalid input and I can end
            //also if the number input it less than 0 or greater than 100, I know that it should throw an error
            for (int i = 0; i < args.length; i++)
            {
                array[i] = Integer.parseInt(args[i]);
                if ((Integer.parseInt(args[i]) > 99) || 
                        (Integer.parseInt(args[i]) <0))
                {
                    System.out.println("Error: Every input must be a "
                            + "positive number between 0 and 99 (inclusive).");
                    System.exit(1);
                }
                if ((i == args.length-1) && (Integer.parseInt(args[i])!=0))
                {
                    System.out.println("Error: The final input must be zero!!");
                    System.exit(1);
                }
            }
        }
        catch (Exception e)
        {
            //Instead of throwing an Exception, I remove the Error Stream so that a problem is easily parsed
            //by the average user
            System.out.println("ERROR: Values out of range.");
            System.out.println("Numbers greater than 99 are not allowed");
            System.exit(1);
        }
        //here I am instantiating the class object so I can use al its methods
        //i am also creating the boolean array, and node so that I can use it in my solver
        //method
        WayFinder obj = new WayFinder();
        boolean[] truth = new boolean[args.length];
        obj.node = obj.setNode(0, 'R');
        obj.solver(array,0, truth, obj.node);
        if (obj.solutionlist.size()<1)
        {
            System.out.println("No way through this puzzle.");
            System.exit(1);
        }
        obj.printer(obj.solutionlist, array);
        System.out.printf("There are %d ways through the puzzle.", 
               obj.solutionlist.size());
        

        
    }
    
    //this is my custom linked list node class that includes
    //the direction and the index of a move as its values
    //it also has a method that adds a node to the end of a list
    //and a method that returns a new clone of a list
    class Node
    {
        Node(int index, char direction)
        {
            this.index = index;
            this.direction = direction;
        }

        int index;
        char direction;
        Node next;
    //iterates through entire list to add to end
    void add(Node next)
    {
        Node current = this;
        while (current.next!=null)
            current = current.next;
        current.next = next;
    }

    //copies every value in one linked list to another
    Node cloner()
    {
        Node current = this;
        Node head = new Node(this.index, this.direction);
        Node headcurrent = head;
        
        int tempint; char tempdirection;
        current=current.next;
        while (current!=null)
        {
            tempint = current.index;
            tempdirection = current.direction;
            headcurrent.next = new Node(tempint, tempdirection);
            current = current.next;
            headcurrent = headcurrent.next;
            
        }
        return head;
        
        
    }
    }
}
