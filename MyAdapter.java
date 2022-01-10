public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Places> eczaneler =new ArrayList<>();
    private int imgId;
    private RecyclerViewClickInterface mrecyclerViewClickInterface;

    public MyAdapter(RecyclerViewClickInterface recyclerViewClickInterface){
        this.mrecyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_card,parent,false);
        MyViewHolder holder = new MyViewHolder(view,mrecyclerViewClickInterface);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.placeNameText.setText(eczaneler.get(position).getName());
        holder.placeAddressText.setText(eczaneler.get(position).getVicinity());
        holder.mesafeText.setText(String.valueOf(eczaneler.get(position).getDistance())+" m");
        holder.myImage.setImageResource(imgId);
    }

    @Override
    public int getItemCount() {
        return eczaneler.size();
    }

    public void setEczaneler(ArrayList<Places> eczaneler,int imgId) {
            this.eczaneler = eczaneler;
            this.imgId = imgId;
            notifyDataSetChanged();

    }

    public ArrayList<Places> getEczaneler(){
        return this.eczaneler;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView myImage;
        private TextView placeNameText, placeAddressText, mesafeText;
        private CardView parent;
        RecyclerViewClickInterface recyclerViewClickInterface;

        public MyViewHolder(@NonNull View itemView, RecyclerViewClickInterface recyclerViewClickInterface) {
            super(itemView);
            placeNameText = itemView.findViewById(R.id.placesNameTxt);
            placeAddressText = itemView.findViewById(R.id.placesAdresTxt);
            mesafeText = itemView.findViewById(R.id.mesafeTxt);
            myImage = itemView.findViewById(R.id.placeImage);
            parent = itemView.findViewById(R.id.parent);
            this.recyclerViewClickInterface = recyclerViewClickInterface;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            recyclerViewClickInterface.onItemClick(getAdapterPosition());
        }
    }

}
