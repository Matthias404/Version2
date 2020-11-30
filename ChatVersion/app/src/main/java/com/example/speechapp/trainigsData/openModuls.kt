package com.example.speechapp.trainigsData

import opennlp.tools.doccat.DoccatModel
import opennlp.tools.doccat.DocumentCategorizerME
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import java.io.FileInputStream


class openModuls {

    public fun posModel(name: String): POSTaggerME{
        val modelIn = FileInputStream("en-pos-maxent.bin")
        val model = POSModel(modelIn)
        return POSTaggerME(model)
    }
    public fun tokenModel(name: String):TokenizerME{
        val modelIn = FileInputStream("de-token.bin")
        val model = TokenizerModel(modelIn)
        return TokenizerME(model)
    }
    public fun trainDoccat(){


    }

}