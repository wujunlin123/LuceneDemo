package Utils;

import java.io.File;
import java.util.Arrays;

//生成ztree标准json数据格式
public
class Tree1
{
    public static void main(String[] args)
    {
        Tree1 d = new Tree1();
        System.out.println(  d.scan("E:\\UserLog"));


    }

    public String scan(String path) {
        File f = new File(path);
        if (f.isDirectory())
        {
            scan(new File(path));
            buf.delete(buf.length() - 2, buf.length());
        }
        else System.out.format("{name : \"%s\"}", f.getName());
        return buf.toString();
    }

    StringBuilder buf;
    int kai = 0;
    public Tree1() {
        buf = new StringBuilder();
    }

    public void println() {
        System.out.println(buf);
    }

    private void scan(File f) {
        if (f.isDirectory())
        {
            buf.append(space(kai)).append("{\n").append(space(++kai)).append("name : \"").append(f.getName()).append("\",\n").append(space(kai)).append("children : [\n");
            kai++;
            Arrays.asList(f.listFiles()).forEach(this::scan);
            buf.delete(buf.length() - 2, buf.length());
            buf.append("\n").append(space(--kai)).append("]\n").append(space(--kai)).append("},\n");
        }
        else buf.append(space(kai)).append("{\n").append(space(++kai)).append("name : \"").append(f.getName()).append("\"\n").append(space(--kai)).append("},\n");
    }

    public String space(int kai) {
        if (kai <= 0)
        {
            return "";
        }
        char[] cs = new char[kai<<1];
        Arrays.fill(cs, ' ');
        return new String(cs, 0, cs.length);
    }
}