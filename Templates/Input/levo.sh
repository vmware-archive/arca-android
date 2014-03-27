export PATH=$PATH:~/go/bin; cd ../Output;

levo -q -k com.chat -p Chat -t ../Input/android -f sync -m "Post id:long text:string user_id:long" -m "User id:long name:string age:int"
#levo -q -k com.chat -p Chat -t ../Input/rails -m "User id:long name:string age:int" -m "Post id:long text:string user_id:long"