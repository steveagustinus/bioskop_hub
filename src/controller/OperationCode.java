package src.controller;

public class OperationCode {

    public class GenerateSeat {
        public final static int SUCCESS = 0;
        public final static int ANYEXCEPTION = -99;
    }

    public class AddNewStudio {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDSTUDIO = -1;
        public final static int INVALIDIDSTUDIO = -2;
        public final static int IDSTUDIOEXISTS = -3;
        public final static int EMPTYIDCINEMA = -4;
        public final static int INVALIDIDCINEMA = -5;
        public final static int EMPTYSTUDIOCLASS = -6;
        public final static int EMPTYSTUDIOTYPE = -7;
        public final static int FAILONSEATGENERATION = -8;
        public final static int ANYEXCEPTION = -99;
    }

    public class EditStudio {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDSTUDIO = -1;
        public final static int EMPTYSTUDIOCLASS = -2;
        public final static int EMPTYSTUDIOTYPE = -3;
        public final static int ANYEXCEPTION = -99;
    }

    public class DeleteStudio {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDSTUDIO = -1;
        public final static int ANYEXCEPTION = -99;
    }

    public class AddNewCinema {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDCINEMA = -1;
        public final static int INVALIDIDCINEMA = -2;
        public final static int EMPTYNAMA = -3;
        public final static int EMPTYALAMAT = -4;
        public final static int EMPTYKOTA = -5;
        public final static int EMPTYFOTO = -6;
        public final static int IDCINEMAEXISTS = -7;
        public final static int ANYEXCEPTION = -99;
    }

    public class EditCinema {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDCINEMA = -1;
        public final static int EMPTYNAMA = -2;
        public final static int EMPTYALAMAT = -3;
        public final static int EMPTYKOTA = -4;
        public final static int EMPTYFOTO = -5;
        public final static int ANYEXCEPTION = -99;
    }

    public class DeleteCinema {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDCINEMA = -1;
        public final static int ANYEXCEPTION = -99;

    }

    public class AddNewMovie {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDMOVIE = -1;
        public final static int EMPTYJUDUL = -2;
        public final static int EMPTYRELEASEDATE = -3;
        public final static int EMPTYDIRECTOR = -4;
        public final static int EMPTYLANGUAGE = -5;
        public final static int EMPTYDURASI = -6;
        public final static int INVALIDDURASI = -7;
        public final static int EMPTYSINOPSIS = -8;
        public final static int EMPTYFOTO = -9;
        public final static int IDMOVIEEXISTS = -10;
        public final static int ANYEXCEPTION = -99;
    }

    public class EditMovie {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDMOVIE = -1;
        public final static int EMPTYJUDUL = -2;
        public final static int EMPTYRELEASEDATE = -3;
        public final static int EMPTYDIRECTOR = -4;
        public final static int EMPTYLANGUAGE = -5;
        public final static int EMPTYDURASI = -6;
        public final static int INVALIDDURASI = -7;
        public final static int EMPTYSINOPSIS = -8;
        public final static int EMPTYFOTO = -9;
        public final static int ANYEXCEPTION = -99;
    }

    public class DeleteMovie {
        public final static int SUCCESS = 0;
        public final static int EMPTYIDMOVIE = -1;
        public final static int ANYEXCEPTION = -99;
    }

    public class addFnB{
        public final static int SUCCESS=0;
        public final static int EMPTYNAME=-1;
        public final static int EMPTYHARGA=-2;
        public final static int EMPTYDESCRIPTION=-3;
        public final static int ANYEXCEPTION=-99;
    }

    public class DeleteFnB {
        public final static int SUCCESS = 0;
        public final static int ANYEXCEPTION = -99;
    }
    
    public class EditFnB{
        public final static int SUCCESS=0;
        public final static int EMPTYNAME=-1;
        public final static int EMPTYHARGA=-2;
        public final static int EMPTYDESCRIPTION=-3;
        public final static int ANYEXCEPTION=-99;
    }
}
