package husacct.analyse.task.reconstruct.mojo;

import org.apache.log4j.Logger;

import husacct.analyse.task.AnalyseTaskControl;

public class MoJo {
	private final Logger logger = Logger.getLogger(AnalyseTaskControl.class);
    public void executeMojo(String[] args, String algorithmName) {
        try
        {
            String sourceFile = null, targetFile = null, relFile = null;
            MoJoCalculator mjc;
            if (args.length < 2 || args.length > 4)
            {
                showerrormsg();
            }
            sourceFile = args[0];
            targetFile = args[1];
            if (args.length > 2)
            {
                /* -m+ indicates single direction MoJoPlus */
                if (args[2].equalsIgnoreCase("-m+"))
                {
                    mjc = new MoJoCalculator(sourceFile, targetFile, relFile);
                    logger.info(mjc.mojoplus());
                }
                else
                /* -b+ indicates double direction MoJoPlus */
                if (args[2].equalsIgnoreCase("-b+"))
                {
                    mjc = new MoJoCalculator(sourceFile, targetFile, relFile);
                    long one = mjc.mojoplus();
                    mjc = new MoJoCalculator(targetFile, sourceFile, relFile);
                    long two = mjc.mojoplus();
                    logger.info(Math.min(one, two));
                }
                else
                /* -b indicates double direction MoJo */
                if (args[2].equalsIgnoreCase("-b"))
                {
                    mjc = new MoJoCalculator(sourceFile, targetFile, relFile);
                    long one = mjc.mojo();
                    mjc = new MoJoCalculator(targetFile, sourceFile, relFile);
                    long two = mjc.mojo();
                    logger.info(Math.min(one, two));
                }
                else
                /* -fm asks for MoJoFM value */
                if (args[2].equalsIgnoreCase("-fm"))
                {
                    mjc = new MoJoCalculator(sourceFile, targetFile, relFile);
                    String messagePart1 = "The algorithm \""+ algorithmName +"\" has a mojo percentage of ";
                    String messagePart2 = "% compared to the created golden standard.";
                    logger.info(messagePart1 + mjc.mojofm() + messagePart2);
                }
                else
                // -e indicates EdgeMoJo (requires extra argument)
                if (args[2].equalsIgnoreCase("-e"))
                {
                    if (args.length == 4)
                    {
                        relFile = args[3];
                        mjc = new MoJoCalculator(sourceFile, targetFile, relFile);
                        logger.info(mjc.edgemojo());
                    }
                    else
                    {
                        showerrormsg();
                    }
                }
                else
                {
                    showerrormsg();
                }

            }
            else
            {
                mjc = new MoJoCalculator(sourceFile, targetFile, relFile);
                logger.info(mjc.mojo());
            }
        }
        catch (RuntimeException e)
        {
            logger.info(e.getMessage());
            logger.info(e.getMessage());
            logger.info(e.getMessage());
            logger.info(e.getMessage());
            logger.info(e.getMessage());
            logger.info(e.getMessage());
        }
    }

    private static void showerrormsg() {
        /*logger.info("");
        logger.info("Please use one of the following:");
        logger.info("");
        logger.info("java mojo.MoJo a.rsf b.rsf");
        logger.info("  calculates the one-way MoJo distance from a.rsf to b.rsf");
        logger.info("java mojo.MoJo a.rsf b.rsf -fm");
        logger.info("  calculates the MoJoFM distance from a.rsf to b.rsf");
        logger.info("java mojo.MoJo a.rsf b.rsf -b");
        logger.info("  calculates the two-way MoJo distance between a.rsf and b.rsf");
        logger.info("java mojo.MoJo a.rsf b.rsf -e r.rsf");
        logger.info("  calculates the EdgeMoJo distance between a.rsf and b.rsf");
        logger.info("java mojo.MoJo a.rsf b.rsf -m+");
        logger.info("  calculates the one-way MoJoPlus distance from a.rsf to b.rsf");
        logger.info("java mojo.MoJo a.rsf b.rsf -b+");
        logger.info("  calculates the two-way MoJoPlus distance between a.rsf and b.rsf");*/
    }

}
