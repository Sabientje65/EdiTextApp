package dcictwebs102859.nl.editext;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server_Connection_Class {

    /*Template



    BufferedInputStream in;
    BufferedOutputStream out;

    @Override
    public void run(){
        Socket sock = null;

        try {
            sock = new Socket("MY IP", 6666);
        } catch (IOException ioe) {
            System.out.println("Message: " + ioe);
        }

        if (sock == null)
            return;

        try{
            out = new BufferedOutputStream(sock.getOutputStream());
            out.flush();
            in = new BufferedInputStream(sock.getInputStream());

            PrintWriter write = new PrintWriter(out, true);

            //code goes here

            write.close();
            out.close();
            in.close();
            sock.close();
        }catch(IOException ioe){
            System.out.println("Message: " + ioe.getMessage());
        }
    }



    */


    static class Login_Class implements Runnable{
        BufferedInputStream in;
        BufferedOutputStream out;

        @Override
        public void run() {
            Socket sock = null;

            try {
                sock = new Socket("MY IP", 6666);
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe);
            }

            try{
                out = new BufferedOutputStream(sock.getOutputStream());
                out.flush();
                in = new BufferedInputStream(sock.getInputStream());

                PrintWriter write = new PrintWriter(out, true);

                String outputLine = "@usr" + Login_Activity.usernameText + "@pass" + Login_Activity.passwordText;

                write.println(outputLine);

                StringBuilder inputLine = new StringBuilder();

                while (true){
                    int input = in.read();

                    if(input == -1)
                        break;

                    inputLine.append((char)input);
                }

                System.out.println(inputLine);

                if(inputLine.toString().contains("id"))
                    Login_Activity.userId = Integer.parseInt(inputLine.substring(inputLine.indexOf("id") + 3, inputLine.length() -2));


                write.close();
                out.close();
                in.close();
                sock.close();

            }catch(IOException ioe){
                System.out.println("Message: " + ioe.getMessage());
            }
        }
    }

    static class GetBlobButtonClass implements Runnable{
        BufferedInputStream in;
        BufferedOutputStream out;

        @Override
        public void run(){
            Socket sock = null;

            try {
                sock = new Socket("MY IP", 6666);
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe);
            }

            if (sock == null)
                return;

            try{
                out = new BufferedOutputStream(sock.getOutputStream());
                out.flush();
                in = new BufferedInputStream(sock.getInputStream());

                PrintWriter write = new PrintWriter(out, true);

                String username = Login_Activity.usernameText;

                String outputLine = "@bids" + Login_Activity.userId;

                write.println(outputLine);

                StringBuilder inputLine = new StringBuilder();

                while(true){
                    int input = in.read();

                    if(input == -1)
                        break;

                    inputLine.append((char)input);
                }

                String inputLineString = inputLine.toString();

                System.out.println(inputLineString);

                StringTokenizer blobIdToken = new StringTokenizer(inputLineString, "@id", false);

                while (blobIdToken.hasMoreTokens()) {
                    String token = blobIdToken.nextToken();

                    while(token.contains("\n"))
                        token = token.replaceAll("\n", "");
                    while(token.contains("\r"))
                        token = token.replaceAll("\r", "");
                    while(token.contains("\t"))
                        token = token.replaceAll("\t", "");

                    Blob_Selection_Activity.idList.add(Integer.parseInt(token));
                }

                System.out.println("Done.");

                Login_Activity.done = true;

                write.close();
                out.close();
                in.close();
                sock.close();
            }catch(IOException ioe){
                System.out.println("Message: " + ioe.getMessage());
            }
        }
    }

    static class ViewBlob implements Runnable{
        BufferedInputStream in;
        BufferedOutputStream out;

        @Override
        public void run(){
            Socket sock = null;

            try {
                sock = new Socket("MY IP", 6666);
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe);
            }

            if (sock == null)
                return;

            try{
                out = new BufferedOutputStream(sock.getOutputStream());
                out.flush();
                in = new BufferedInputStream(sock.getInputStream());

                PrintWriter write = new PrintWriter(out, true);

                write.println("@view" + Blob_Selection_Activity.selectedBlobId);

                StringBuilder inBuilder = new StringBuilder();

                while (true){
                    int inputInt = in.read();

                    if(inputInt == -1)
                        break;

                    inBuilder.append((char)inputInt);
                }

                Blob_View_Activity.blobText = inBuilder.toString();

                write.close();
                out.close();
                in.close();
                sock.close();

                Login_Activity.done = true;
            }catch(IOException ioe){
                System.out.println("Message: " + ioe.getMessage());
            }
        }
    }

    static class SaveBlob implements Runnable{
        BufferedInputStream in;
        BufferedOutputStream out;

        @Override
        public void run(){
            Socket sock = null;

            try {
                sock = new Socket("MY IP", 6666);
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe);
            }

            if (sock == null)
                return;

            try{
                out = new BufferedOutputStream(sock.getOutputStream());
                out.flush();
                in = new BufferedInputStream(sock.getInputStream());

                PrintWriter write = new PrintWriter(out, true);

                String outputString = Blob_View_Activity.blobText;

                outputString = outputString.replaceAll("\n", "@nLine");

                write.println("@save" + Blob_Selection_Activity.selectedBlobId + "@id" + outputString);

                while(true){
                    int inputInt = in.read();

                    if (inputInt == -1)
                        break;

                    if(Blob_View_Activity.returnText.equals("-3"))
                        Blob_View_Activity.returnText =  Character.toString((char) inputInt);
                    else
                        Blob_View_Activity.returnText +=  Character.toString((char) inputInt);
                }

                Login_Activity.done = true;

                write.close();
                out.close();
                in.close();
                sock.close();
            }catch(IOException ioe){
                System.out.println("Message: " + ioe.getMessage());
            }
        }
    }

    static class CreateBlob implements Runnable {
        BufferedInputStream in;
        BufferedOutputStream out;

        @Override
        public void run() {
            Socket sock = null;

            try {
                sock = new Socket("MY IP", 6666);
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe);
            }

            if (sock == null)
                return;

            try {
                out = new BufferedOutputStream(sock.getOutputStream());
                out.flush();
                in = new BufferedInputStream(sock.getInputStream());

                PrintWriter write = new PrintWriter(out, true);

                write.println("@create" + Login_Activity.userId);

                while (true){
                    int inputLine = in.read();

                    if(inputLine == -1)
                        break;
                }

                write.close();
                out.close();
                in.close();
                sock.close();

                Login_Activity.done = true;
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe.getMessage());
            }
        }
    }

    static class DeleteBlob implements Runnable{
        BufferedInputStream in;
        BufferedOutputStream out;

        @Override
        public void run(){
            Socket sock = null;

            try {
                sock = new Socket("MY IP", 6666);
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe);
            }

            if (sock == null)
                return;

            try{
                out = new BufferedOutputStream(sock.getOutputStream());
                out.flush();
                in = new BufferedInputStream(sock.getInputStream());

                PrintWriter write = new PrintWriter(out, true);

                write.println("@delete" + Blob_Selection_Activity.selectedBlobId);

                while(true){
                    if(in.read() == -1)
                        break;
                }

                write.close();
                out.close();
                in.close();
                sock.close();

                Login_Activity.done = true;
            }catch(IOException ioe){
                System.out.println("Message: " + ioe.getMessage());
            }
        }
    }

    static class Register implements Runnable{
        BufferedInputStream in;
        BufferedOutputStream out;

        @Override
        public void run(){
            Socket sock = null;

            try {
                sock = new Socket("MY IP", 6666);
            } catch (IOException ioe) {
                System.out.println("Message: " + ioe);
            }

            if (sock == null)
                return;

            try{
                out = new BufferedOutputStream(sock.getOutputStream());
                out.flush();
                in = new BufferedInputStream(sock.getInputStream());

                PrintWriter write = new PrintWriter(out, true);

                write.println("@reg" + Login_Activity.usernameText + "@pass" + Login_Activity.passwordText);

                StringBuilder inputLine = new StringBuilder();

                while(true){
                    int inputInt = in.read();

                    if(inputInt == -1)
                        break;

                    inputLine.append((char) inputInt);
                }

                if(inputLine.toString().contains("id"))
                    Login_Activity.userId = Integer.parseInt(inputLine.substring(inputLine.indexOf("id") + 3, inputLine.length() -2));

                write.close();
                out.close();
                in.close();
                sock.close();

                Login_Activity.done = true;
            }catch(IOException ioe){
                System.out.println("Message: " + ioe.getMessage());
            }
        }
    }
}
