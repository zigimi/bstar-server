import React, {useEffect, useState, useRef} from 'react';
import { Avatar } from "@mui/material";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import styled from "styled-components";
import Button from "@mui/material/Button";

import AvatarUploader from "react-avatar-uploader";
import axios from 'axios';

function ProfileImage(props) {
  const reader = new FileReader();

  const [open, setOpen] = useState(false);

  const [image, setImage] = useState("");

  const fileInput = useRef(null);

  const [info, setInfo] = useState([]);

  useEffect(() => {
    axios.get('setting/info')
      .then(response => setInfo(response.data))
      .catch(error => console.log(error))
  }, []);

  useEffect(() => {
    if(info.image) {
        async function convertURLtoFile(url) {
        const response = await fetch(url);
        const data = await response.blob();
        const ext = url.split(".").pop(); // url 구조에 맞게 수정할 것
        const filename = url.split("/").pop(); // url 구조에 맞게 수정할 것
        const metadata = { type: `image/${ext}` };
        return new File([data], filename, metadata);
      }
       convertURLtoFile(info.image)
        .then((result) => {reader.readAsDataURL(result);});

        reader.onload = () => {
        if (reader.readyState === 2) {
          setImage(reader.result);
        }
      }

      //setImage(require('./src'+info.image).default);

    }
    else {
      setImage("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
    }
  }, [info]);

  const onChange = (e) => {
    if (e.target.files[0]) {
      setImage(e.target.files[0]);
      const formData = new FormData();
      formData.append('multipartFile', e.target.files[0]);

      axios.post('setting/info', formData)
          .then(function (response) {

          }).catch(function (error) {
            alert(error);
          }).then(function() {
              // 항상 실행
          });
    } else {
      //업로드 취소할 시
       setImage(
        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
      );
      return;
    }
    //화면에 프로필 사진 표시
    reader.onload = () => {
      if (reader.readyState === 2) {
        setImage(reader.result);
      }
    }
    reader.readAsDataURL(e.target.files[0]);
  }

  return (
    <div>
      <Avatar
        src={image}
        style={{ margin: "1vw", width: "20vw", height: "20vw" }}
        onClick={() => {
          setOpen(true);
        }}
      />


      <input
        type="file"
        style={{ display: "none" }}
        accept="image/jpg,image/png,image/jpeg"
        name="profile_img"
        onChange={onChange}
        ref={fileInput}
      />

      <Dialog open={open}>
        <DialogTitle>Profile Image</DialogTitle>

        <DialogActions>
          <Button variant="outlined"> 크게 보기 </Button>

          <Button
            variant="outlined"
            onClick={() => {
              fileInput.current.click();
            }}
          >
            변경하기
          </Button>
          <Button
            variant="outlined"
            onClick={() => {
              setOpen(false);
            }}
          >
            닫기
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default ProfileImage;
