import java.util.LinkedList;

public class Md5
{
    static LinkedList <Integer> mdblock = new LinkedList<>();
    static int additiveConst(int index)
    {
        // floor (2^32 * |sin(i+1)|)
        
        long result = (long)Math.floor(Math.abs(Math.sin(index + 1)) * 4294967296.0);
        return (int)result;
    }
    static int F(int B, int C, int D, int i)
    {
        //round1
        if (i < 16)
            return (B & C) | (~B & D);
    
        //round2

        else if (i >= 16 && i < 32)
            return (D & B) | (~D & C);
        //round3
        
        else if (i >= 32 && i < 48)
            return B ^ C ^ D;
        //round4
        
        return (C ^ (B | ~D));
    }

    static int shiftsequence(int i)
    {
        if (i < 16)
        {
            int []array = {7,12,17,22};
            return array[i%4];
        }
        else if (i < 32)
        {
            int []array = {5,9,14,20};
            return array[i%4];
        }
        else if (i < 48)
        {
            int []array = {4,11,16,23};
            return array[i%4];
        }
        else
        {
            int []array = {6,10,15,21};
            return array[i%4];
        }

    }
    static int mdblockAt(int i)
    {

        if (i < 16)
            i = i;

        else if (i < 32)
            i = (i*5+1)%16;

        else if (i < 48)
            i = (i*3+5)%16;

        else if (i < 64)
            i = (i*7)%16;

        int index = i*4;

        return (mdblock.get(index) & 0xFF)
     | ((mdblock.get(index + 1) & 0xFF) << 8)
     | ((mdblock.get(index + 2) & 0xFF) << 16)
     | ((mdblock.get(index + 3) & 0xFF) << 24);
    }

    public static void main(String [] args)
    {
        int A = 0x67452301;
        int B = 0xEFCDAB89;
        int C = 0x98BADCFE;
        int D = 0x10325476;

        int a0 = A;
        int b0 = B;
        int c0 = C;
        int d0 = D;
        Md5 myobj = new Md5();
        int i = 0;
        char []array = args[0].toCharArray();
        System.out.println("array length = " + array.length*8);
        while (i < array.length)
            myobj.mdblock.add((int)array[i++]);
        myobj.mdblock.add(1<<7);

        long textlength = (long)array.length*8;
        int t = 56;

        while ((myobj.mdblock.size() % 64) != 56) {
           myobj.mdblock.add(0);
        }

        for (int j = 0; j < 8; j++) {
            myobj.mdblock.add((int)((textlength >>> (8 * j)) & 0xFF));
        }

        for (int j = 0; j < myobj.mdblock.size(); j++) {
            System.out.print(
                String.format("%8s",
                    Integer.toBinaryString(myobj.mdblock.get(j) & 0xFF)
                ).replace(' ', '0')
            + ' ');
            if ((j+1)%8 == 0)
                System.out.println();
            if ((j+1) % 64 == 0)
                System.out.println("-----------------------------------------------------------------------");
        }

        for (int index = 0; index < 64; index++)
        {

            int temp = A + F(B, C, D, index) + mdblockAt(index) + additiveConst(index);

            temp = Integer.rotateLeft(temp, shiftsequence(index));

            temp = B + temp;

            A = D;
            D = C;
            C = B;
            B = temp;
        }

        A += a0;
        B += b0;
        C += c0;
        D += d0;
        System.out.printf("%02x%02x%02x%02x  ", A & 0xff, (A >>> 8) & 0xff,(A >>> 16) & 0xff,(A >>> 24) & 0xff);
        System.out.printf("%02x%02x%02x%02x  ", B & 0xff, (B >>> 8) & 0xff,(B >>> 16) & 0xff,(B >>> 24) & 0xff);
        System.out.printf("%02x%02x%02x%02x  ", C & 0xff, (C >>> 8) & 0xff,(C >>> 16) & 0xff,(C >>> 24) & 0xff);
        System.out.printf("%02x%02x%02x%02x  ", D & 0xff, (D >>> 8) & 0xff,(D >>> 16) & 0xff,(D >>> 24) & 0xff);
}