export PATH=$PATH:~/go/bin; cd ../Output;

levo -f -k com.chat -p Chat -t ../Input/android -m "Post id:long text:string user_id:long" -m "User id:long name:string age:int"
levo -f -k com.chat -p Chat -t ../Input/rails -m "User id:long name:string age:int" -m "Post id:long text:string user_id:long"