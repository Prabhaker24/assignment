package wiki;



import java.util.*;
import java.util.stream.Collectors;

public class WikiWalker {
    private Map links;


    // TODO: Choose some data structure to implement the site map!

    WikiWalker() {
        // TODO: Any initializations you need
      links=new HashMap<String,TreeMap<String,Integer>>();

    }

    public static void main(String[] args)
    {
        WikiWalker ww=new WikiWalker();
        Scanner input = new Scanner(System. in);
        char c=0;
        System.out.println("enter y to enter article and links");
        c= input.next().charAt(0);
        while(c=='y'||c=='Y')
        {
            System.out.println("enter article name");
            String article= input.next();
            List<String> articleLinks =new ArrayList<>();
            System.out.println("enter links to the article seprated by comma");
            String str=input.next();
            String[] elements = str.split(",");
            articleLinks=Arrays.asList(elements);
            ww.addArticle(article,articleLinks);
            System.out.println("enter y to enter article and links");
            c= input.next().charAt(0);
        }
        ww.print_links();
        System.out.println("enter source for checking hasPath");
        String src=input.next();
        System.out.println("enter destination for checking hasPath");
        String dst=input.next();
        System.out.println(ww.hasPath(src,dst));
        System.out.println("enter trajectory sequence eg (a,b,c)");
        List<String> traj=new ArrayList();
        String tr =input.next();
        String[] elements=tr.split(",");
        traj=Arrays.asList(elements);
        ww.logTrajectory(traj);
        ww.print_links();
        System.out.println("enter source for checking clickthrough");
        src=input.next();
        System.out.println("enter destination for checking clickthrough");
        dst=input.next();
        System.out.println(ww.clickthroughs(src,dst));
        System.out.println("enter source for checking mostLikelyTrajectory");
        src=input.next();
        System.out.println("enter value of k for checking mostLikelyTrajectory");
        int k=input.nextInt();
        System.out.println(ww.mostLikelyTrajectory(src,k));

    }
    public void print_links()
    {
        Set<String> keys=links.keySet();
        for(String key: keys)
        {
            System.out.println(key+links.get(key));
        }

    }

    /**
     * Adds an article with the given name to the site map and associates the
     * given linked articles found on the page. Duplicate links in that list are
     * ignored, as should an article's links to itself.
     *
     * @param articleName
     *            The name of the page's article
     * @param articleLinks
     *            List of names for those articles linked on the page
     */
    public void addArticle(String articleName, List<String> articleLinks) {
        try{
            TreeMap tm=new TreeMap<String,Integer>();
            for(String al:articleLinks) {
                if (al.equalsIgnoreCase(articleName))
                    continue;
                tm.put(al, 0);
            }
            links.put(articleName,tm);
            return;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


       // throw new UnsupportedOperationException();
    }

    /**
     * Determines whether or not, based on the added articles with their links,
     * there is some sequence of links that could be followed to take the user
     * from the source article to the destination.
     *
     * @param src
     *            The beginning article of the possible path
     * @param dest
     *            The end article along a possible path
     * @return boolean representing whether or not that path exists
     */
    public boolean hasPath(String src, String dest) {

    List<String> linkstraversedsofar=new ArrayList<>();
    return hasPathHelp(src, dest, linkstraversedsofar);
       // throw new UnsupportedOperationException();
    }

    public boolean hasPathHelp(String src, String dest,List<String> linkstraversedsofar)
    {

        Set<String> keys=links.keySet();
        if(keys.contains(src)==false)
            return false;
        if (dest.equalsIgnoreCase(src))
            return true;
        TreeMap tm=new TreeMap<String,Integer>();
        tm=(TreeMap)links.get(src);
        Set<String> keysvalues=tm.keySet();
        linkstraversedsofar.add(src);
        if (keysvalues.contains(dest))
            return true;
        else
            for(String s:keysvalues)
            {
                if(linkstraversedsofar.contains(s)==true)
                    continue;
                if(hasPathHelp(s, dest,linkstraversedsofar)==true)
                    return true;
            }

        return false;

    }
    /**
     * Increments the click counts of each link along some trajectory. For
     * instance, a trajectory of ["A", "B", "C"] will increment the click count
     * of the "B" link on the "A" page, and the count of the "C" link on the "B"
     * page. Assume that all given trajectories are valid, meaning that a link
     * exists from page i to i+1 for each index i
     *
     * @param traj
     *            A sequence of a user's page clicks; must be at least 2 article
     *            names in length
     */
    public void logTrajectory(List<String> traj) {
        if(traj.size()<2)
        {
            System.out.println("length is less than 2");
            return;
        }
        TreeMap hm=new TreeMap<String,Integer>();
        for (int i = 0; i < traj.size()-1; i++) {
            hm= (TreeMap)links.get(traj.get(i));
            int count=(Integer)hm.get(traj.get(i+1));
            count++;
            hm.put(traj.get(i+1),count);
            links.put(traj.get(i),hm);

        }
        //throw new UnsupportedOperationException();
    }

    /**
     * Returns the number of clickthroughs recorded from the src article to the
     * destination article. If the destination article is not a link directly
     * reachable from the src, returns -1.
     *
     * @param src
     *            The article on which the clickthrough occurs.
     * @param dest
     *            The article requested by the clickthrough.
     * @throws IllegalArgumentException
     *             if src isn't in site map
     * @return The number of times the destination has been requested from the
     *         source.
     */
    public int clickthroughs(String src, String dest) {
        Set<String> keys=links.keySet();
        if (keys.contains(src)==false)
            throw new IllegalArgumentException();
        TreeMap hm=new TreeMap<String,Integer>();
        hm=(TreeMap)links.get(src);
        if(hm.containsKey(dest)==false)
            return -1;
        int count=(int)hm.get(dest);
        return count;


        //throw new UnsupportedOperationException();
    }

    /**
     * Based on the pattern of clickthrough trajectories recorded by this
     * WikiWalker, returns the most likely trajectory of k clickthroughs
     * starting at (but not including in the output) the given src article.
     * Duplicates and cycles are valid output along a most likely trajectory. In
     * the event of a tie in max clickthrough "weight," this method will choose
     * the link earliest in the ascending alphabetic order of those tied.
     *
     * @param src
     *            The starting article of the trajectory (which will not be
     *            included in the output)
     * @param k
     *            The maximum length of the desired trajectory (though may be
     *            shorter in the case that the trajectory ends with a terminal
     *            article).
     * @return A List containing the ordered article names of the most likely
     *         trajectory starting at src.
     */
    public List<String> mostLikelyTrajectory(String src, int k) {
        List<String> nextarticles=new ArrayList<>();
        if(k==0)
            return nextarticles;
        TreeMap hm=new TreeMap<String,Integer>();
        if(links.containsKey(src)==false)
            return nextarticles;
        hm=(TreeMap)links.get(src);
        nextarticles.add(src);
        if(hm.isEmpty()==true)
            return nextarticles;
        Set<String> keysvalues=hm.keySet();
        int maxCLickThrough=0;
        String maxarticle="";
        for(String nextarticle:keysvalues)
        {
           int tempcount=(int)hm.get(nextarticle);
           if(tempcount>maxCLickThrough)
           {
               maxCLickThrough=tempcount;
               maxarticle=nextarticle;
           }
        }
        //System.out.println("value of k = "+ k +nextarticles);
        if(maxCLickThrough!=0)
            nextarticles.addAll(mostLikelyTrajectory(maxarticle,k-1));
        return nextarticles;
        //throw new UnsupportedOperationException();
    }

}
