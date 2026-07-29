import java.util.LinkedList;

public class Sha1
{
    static LinkedList <Integer> mdblock = new LinkedList<>();
    static LinkedList<Integer> extendedWord;

    static int additiveConst(int i)
    {
        if (i <= 19)
            return 0x5A827999;
    
        //round2

        else if (i >= 20 && i <= 39)
            return 0x6ED9EBA1;
        //round3

        else if (i >= 40 && i <= 59)
            return 0x8F1BBCDC;
        //round4

        return 0xCA62C1D6;
    }
    static int F(int B, int C, int D, int i)
    {
        //round1
        if (i <= 19)
            return (B & C) | (~B & D);
    
        //round2

        else if (i >= 20 && i <= 39)
            return B ^ C ^ D;
        //round3
        
        else if (i >= 40 && i <= 59)
            return (B & C) | (B & D) | (C & D);
        //round4
        else
            return B ^ C ^ D;
    }
    private static int getWord(int wordIndex) {
        int i = wordIndex * 4;
        return ((extendedWord.get(i) & 0xFF) << 24) |
       ((extendedWord.get(i + 1) & 0xFF) << 16) |
       ((extendedWord.get(i + 2) & 0xFF) << 8) |
       (extendedWord.get(i + 3) & 0xFF);
    }
    public static void main(String [] args)
    {
        int h0 = 0x67452301;
        int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;
        int A = h0;
        int B = h1;
        int C = h2;
        int D = h3;
        int E = h4;

        char []array = args[0].toCharArray();
        System.out.println("array length = " + array.length*8);
        for (int i = 0; i < array.length; i++)
            mdblock.add((int)array[i]);
        mdblock.add(1<<7);

        long textlength = (long)array.length*8;
        int t = 56;

        while ((mdblock.size() % 64) != 56) {
           mdblock.add(0);
        }

        for (int j = 7; j >= 0; j--) {
            mdblock.add((int)((textlength >> (8 * j)) & 0xFF));
        }
        

        while (mdblock.size() != 0)
        {
            extendedWord = new LinkedList<>(mdblock.subList(0, 64));
      
            System.out.println("size extende = " + extendedWord.size());
            for (int i = 16; i < 80; i++)
            {
                int value = Integer.rotateLeft(getWord(i-3) ^ getWord(i-8)
                            ^ getWord(i-14) ^ getWord(i-16), 1);

                extendedWord.add((value >>> 24) & 0xFF);
                extendedWord.add((value >>> 16) & 0xFF);
                extendedWord.add((value >>> 8) & 0xFF);
                extendedWord.add(value & 0xFF);
            }
            for (int index = 0; index < 80; index++)
            {
                int temp = Integer.rotateLeft(A, 5) + F(B, C, D, index) + E + getWord(index) + additiveConst(index);
                E = D;
                D = C;
                C = Integer.rotateLeft(B, 30);
                B = A;
                A = temp;
            }
            h0 += A;
            h1 += B;
            h2 += C;
            h3 += D;
            h4 += E;
            A=h0;
            B=h1;
            C=h2;
            D=h3;
            E=h4;
            mdblock.subList(0, 64).clear();
        }
        System.out.printf("%08x %08x %08x %08x %08x\n", h0, h1, h2, h3, h4);
    }
}