package org.ufba.utils;

import org.ufba.enums.NodeType;
import org.ufba.node.BitSeparator;
import org.ufba.node.Node;
import org.ufba.node.SerializebleEntity;
import org.ufba.node.WordNode;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.ufba.Constants.BIT_SEPARATOR_FILE;
import static org.ufba.Constants.DICTIONARY_FILE;


public class FileUtils<T>{

    String inputFile;


    public FileUtils(String inputFile){
        this.inputFile = inputFile;
    }

    protected static FileUtils from(NodeType nodeType){
        if(nodeType.equals(NodeType.bitSeparator)){
            return new FileUtils(BIT_SEPARATOR_FILE);
        }

        return new FileUtils(DICTIONARY_FILE);
    }


    public void writeObject(SerializebleEntity object){
        int fileSize = (int) getFileSize();
        object.setDiskOffset(fileSize);
        byte [] bytes = convertNodeToBytes((Node) object);
        writeBytes(bytes, fileSize, bytes.length);
    }

    public void writeObject(SerializebleEntity object, int position){
        object.setDiskOffset(position);
        byte [] bytes = convertNodeToBytes((Node) object);
        writeBytes(bytes, position, bytes.length);
    }

    public void writeBytes(byte [] bytes, int offset, int length){
        byte [] fixedBytes = new byte[length];

        System.arraycopy(bytes, 0, fixedBytes, 0, bytes.length);

        try {
            RandomAccessFile raf = new RandomAccessFile(inputFile, "rw");
            raf.seek(offset);
            raf.write(fixedBytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public byte[] readBytes(int offset, int bytesSize){
        byte[] result = new byte[bytesSize];
        try{
            RandomAccessFile raf = new RandomAccessFile(inputFile, "r");
            raf.seek(offset);
            raf.read(result);
        } catch (IOException e){
            System.out.println("Houve um erro ao ler os bytes");
        }

        return result;
    }

    public static byte[] convertNodeToBytes(Node node){
        switch (node.getNodeType()){
            case bitSeparator:
                return convertBitSeparatorToBytes((BitSeparator) node);
            case word:
                return convertWordNodeToBytes((WordNode) node);
            default:
                throw new Error("invalid node type");
        }
    }

    public static byte[] convertWordNodeToBytes(WordNode node){
        ByteBuffer buffer = ByteBuffer.allocate(node.getBytesLength());
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(new String(node.getWordCharacters()).getBytes());
        buffer.put(node.getWordClass().toString().getBytes());
        buffer.putInt(node.getNumberOfTranslations());
        buffer.put(new String(node.getPointerToDiskCharacters()).getBytes());

        for(int i=0; i<node.getWordTranslationsCharacters().length; i++){
            buffer.put(new String(node.getWordTranslationsCharacters()[i]).getBytes());
        }

        return buffer.array();
    }

    public static byte[] convertBitSeparatorToBytes(BitSeparator node){
        ByteBuffer buffer = ByteBuffer.allocate(node.getBytesLength());
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(node.getBit());
        buffer.put(new String(node.getPointerLeftCharacters()).getBytes());
        buffer.put(new String(node.getPointerRightCharacters()).getBytes());
        buffer.put(new String(node.getPointerToDiskCharacters()).getBytes());
        return buffer.array();
    }

    public Object convertBytesToObject(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object result = null;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Houve um erro ao converter bytes em objetos");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        return result;
    }

    public void printBytes(byte[] bytes){
        for(int i=0; i < bytes.length; i++){
            System.out.print(bytes[i]+" ");
        }
        System.out.println();
    }

    public long getFileSize(){
        return new File(inputFile).length();
    }

    public boolean isFileEmpty(){
        return this.getFileSize() == 0;
    }

    public void dumpFile(){
        byte [] bytes = new byte[(int) getFileSize()];
        try(
            FileInputStream fileInputStream = new FileInputStream(inputFile);
        ){
            fileInputStream.read(bytes);
        } catch (Exception e){
            e.printStackTrace();
        }
        printBytes(bytes);
    }

    public void setInputFile(String inputFile){
        this.inputFile = inputFile;
    }


}
